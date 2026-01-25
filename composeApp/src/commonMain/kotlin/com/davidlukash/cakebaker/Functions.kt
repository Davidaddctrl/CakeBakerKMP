package com.davidlukash.cakebaker

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.davidlukash.cakebaker.data.log.Log
import com.davidlukash.cakebaker.engine.CakeBakerEngine
import com.davidlukash.cakebaker.logger.AppLogger
import com.davidlukash.cakebaker.logger.CompoundAppLogger
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.floor
import kotlin.math.pow
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

val globalDecimalMode = DecimalMode(
    decimalPrecision = 34,
    roundingMode = RoundingMode.FLOOR,
    scale = 1
)

val logger = CompoundAppLogger().also {
    it.registerLogger(
        object : AppLogger() {
            override fun appendLog(log: Log) {
                println(log.toLogString())
            }
        }
    )
}

fun BigDecimal.log10(): Int {
    return this.toString().split("+")[1].toInt()
}

@OptIn(ExperimentalTime::class)
fun currentLocalTime(): LocalTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time

val debugTimestampFormat = LocalTime.Format {
    char('[')
    hour()
    char(':')
    minute()
    char(':')
    second()
    char('.')
    secondFraction(3)
    char(']')
}

fun Boolean.toInt() = if (this) 1 else 0

fun Int.toBoolean() = this == 1

fun toEngNotation(number: BigDecimal): String {
    if (number < 1000.toBigDecimal()) {
        return number.toPlainString().let {
            if (it.endsWith(".0")) {
                it.removeSuffix(".0")
            } else it
        }
    }
    val suffixes = listOf("", "K", "M", "B", "T", "Qa", "Qt", "Sx", "Sp", "Oc", "No")
    val index = floor(number.log10().toDouble() / 3.0).toInt()
    val scaled = (number.divide(
        1000.0.pow(index).toBigDecimal(),
        decimalMode = globalDecimalMode
    )).roundToDigitPositionAfterDecimalPoint(1L, RoundingMode.FLOOR)
    val string = scaled.toPlainString().let {
        if (it.endsWith(".0")) {
            it.removeSuffix(".0")
        } else it
    }
    return if ((suffixes.size - 1) >= index)
        "$string${suffixes[index]}"
    else number.toString()
}

val Color.luminance
    get(): Float {
        fun convert(value: Float): Float =
            if (value <= 0.03928f) value / 12.92f else ((value + 0.055f) / 1.055f).pow(2.4f)

        val rLinear = convert(red)
        val gLinear = convert(green)
        val bLinear = convert(blue)

        return 0.2126f * rLinear + 0.7152f * gLinear + 0.0722f * bLinear
    }

val Color.isDark
    get(): Boolean {
        return luminance <= 0.5f
    }

val Color.isLight
    get(): Boolean {
        return luminance > 0.5f
    }

fun lerp(a: Float, b: Float, t: Float): Float {
    return a + (b - a) * t
}

fun mapDouble(n: Double, inMin: Double, inMax: Double, outMin: Double, outMax: Double): Double {
    return (n - inMin) * (outMax - outMin) / (inMax - inMin) + outMin
}

fun mapDoubleBiased(
    n: Double,
    inMin: Double,
    inMax: Double,
    outMin: Double,
    outMax: Double,
    bias: Double
): Double {
    val bias = -bias
    val normalized = (n - inMin) / (inMax - inMin)

    val biased = normalized.pow(if (bias >= 0.0) (bias + 1.0) else (1.0 - bias))
    return if (bias >= 0) outMin + biased * (outMax - outMin) else outMax - biased * (outMax - outMin)
}

fun weightedRandom(weight: Double, max: Double, random: Random): Double {
    val n = random.nextDouble()
    val weighted = n.pow(1f / weight)

    return weighted * max
}

fun weightedRandomInt(weight: Double, max: Int, random: Random): Int {
    return minOf(floor(weightedRandom(weight, max.toDouble(), random)).toInt(), max - 1)
}

fun <T> weightedRandomItem(weight: Double, items: List<T>, random: Random): T? {
    return items.getOrNull(weightedRandomInt(weight, items.size, random))
}

fun secondsToString(totalSeconds: Double): String {
    val minutes = floor(totalSeconds / 60.0).toInt()
    val seconds = floor(totalSeconds % 60.0).toInt()
    return if (minutes == 0) {
        "$seconds seconds"
    } else {
        "${minutes}m ${seconds}s"
    }
}

val json = Json {
    prettyPrint = true
    allowStructuredMapKeys = true
}

expect fun Modifier.horizontalDragCursor(): Modifier
expect fun Modifier.verticalDragCursor(): Modifier

fun Modifier.horizontalRowScroll(
    coroutineScope: CoroutineScope,
    scrollState: ScrollState,
    doScrollWheel: Boolean = true,
    reversed: Boolean = false
): Modifier {
    return this.pointerInput(Unit) {
        detectHorizontalDragGestures { change, dragAmount ->
            coroutineScope.launch {
                scrollState.scrollBy(if (reversed) dragAmount else -dragAmount)
            }
        }
    }.then(
        if (doScrollWheel)
            Modifier.pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val scrollDelta = event.changes.firstOrNull()?.scrollDelta?.y
                        if (scrollDelta != null && scrollDelta != 0f) {
                            coroutineScope.launch {
                                scrollState.scrollBy(if (reversed) (scrollDelta * -96) else (scrollDelta * 96))
                            }
                        }
                    }
                }
            }
        else Modifier
    )
}

//@OptIn(ExperimentalUuidApi::class)
//fun <T> withErrorHandling(appLogger: AppLogger, finallyBlock: () -> Unit = {}, block: () -> T): Result<T> {
//    try {
//        return Result.success(block())
//    } catch (e: CancellationException) {
//        throw e
//    } catch (e: LanguageException) {
//        if (appLogger.getDebugConsole() == ConsoleType.NONE)
//            appLogger.setDebugConsole(ConsoleType.POPUP)
//        appLogger.appendLog(Log(e.toString() + e.origins?.toTraceString(), LogType.ERROR))
//        return Result.failure(e)
//    } catch (e: Exception) {
//        if (appLogger.getDebugConsole() == ConsoleType.NONE)
//            appLogger.setDebugConsole(ConsoleType.POPUP)
//        appLogger.appendLog(Log(e.stackTraceToString(), LogType.ERROR))
//        return Result.failure(e)
//    } finally {
//        finallyBlock()
//    }
//}
//
//fun <T> DataViewModel.withErrorHandling(finallyBlock: () -> Unit = {}, block: () -> T): Result<T> =
//    withErrorHandling(this.uiViewModel, finallyBlock, block)
//
//@OptIn(ExperimentalUuidApi::class)
//suspend fun <T> withErrorHandlingAsync(
//    appLogger: AppLogger,
//    finallyBlock: suspend () -> Unit = {},
//    block: suspend () -> T
//): Result<T> {
//    try {
//        return Result.success(block())
//    } catch (e: CancellationException) {
//        throw e
//    } catch (e: LanguageException) {
//        if (appLogger.getDebugConsole() == ConsoleType.NONE)
//            appLogger.setDebugConsole(ConsoleType.POPUP)
//        appLogger.appendLog(Log(e.toString() + e.origins?.toTraceString(), LogType.ERROR))
//        return Result.failure(e)
//    } catch (e: Exception) {
//        if (appLogger.getDebugConsole() == ConsoleType.NONE)
//            appLogger.setDebugConsole(ConsoleType.POPUP)
//        appLogger.appendLog(Log(e.stackTraceToString(), LogType.ERROR))
//        return Result.failure(e)
//    } finally {
//        finallyBlock()
//    }
//}
//
//suspend fun <T> DataViewModel.withErrorHandlingAsync(
//    finallyBlock: suspend () -> Unit = {},
//    block: suspend () -> T
//): Result<T> =
//    withErrorHandlingAsync(this.uiViewModel, finallyBlock, block)

fun <T> withResult(finallyBlock: () -> Unit = {}, block: () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) { throw e }
    catch (e: Exception) {
        logger.logError(e)
        Result.failure(e)
    } finally { finallyBlock() }
}

suspend fun <T> withResultSuspend(finallyBlock: suspend () -> Unit = {}, block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) { throw e }
    catch (e: Exception) {
        logger.logError(e)
        Result.failure(e)
    } finally { finallyBlock() }
}

expect fun dumpFunctionsToFile(engine: CakeBakerEngine)

fun BigDecimal.roundTo1dp(): BigDecimal = this.roundSignificand(globalDecimalMode)

expect val platform: com.davidlukash.cakebaker.data.Platform

fun <T : Any> T?.takeOrDefaultWithWarn(warnMessage: String, default: T): T {
    if (this == null) {
        logger.logWarn(warnMessage)
        return default
    }
    return this
}

fun <T : Any> T?.takeOrNullWithWarn(warnMessage: String): T? {
    if (this == null) {
        logger.logWarn(warnMessage)
        return null
    }
    return this
}