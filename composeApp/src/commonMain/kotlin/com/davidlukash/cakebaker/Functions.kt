package com.davidlukash.cakebaker

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.davidlukash.cakebaker.data.ConsoleType
import com.davidlukash.cakebaker.data.Log
import com.davidlukash.cakebaker.data.LogType
import com.davidlukash.cakebaker.viewmodel.CakeBakerEngine
import com.davidlukash.cakebaker.viewmodel.DataViewModel
import com.davidlukash.jsonmath.engine.basic.toTraceString
import com.davidlukash.jsonmath.engine.normal.LanguageException
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
import kotlin.uuid.ExperimentalUuidApi

val globalDecimalMode = DecimalMode(
    decimalPrecision = 34,
    roundingMode = RoundingMode.FLOOR,
    scale = 1
)

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
    return "$string${suffixes[index]}"
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
    reversed: Boolean = false
): Modifier {
    return this.pointerInput(Unit) {
        detectHorizontalDragGestures { change, dragAmount ->
            coroutineScope.launch {
                scrollState.scrollBy(if (reversed) dragAmount else -dragAmount)
            }
        }
    }
        .pointerInput(Unit) {
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
}

@OptIn(ExperimentalUuidApi::class)
fun <T> withErrorHandling(appLogger: AppLogger, finallyBlock: () -> Unit = {}, block: () -> T): Result<T> {
    try {
        return Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: LanguageException) {
        if (appLogger.getDebugConsole() == ConsoleType.NONE)
            appLogger.setDebugConsole(ConsoleType.POPUP)
        appLogger.appendLog(Log(e.toString() + e.origins?.toTraceString(), LogType.ERROR))
        return Result.failure(e)
    } catch (e: Exception) {
        if (appLogger.getDebugConsole() == ConsoleType.NONE)
            appLogger.setDebugConsole(ConsoleType.POPUP)
        appLogger.appendLog(Log(e.stackTraceToString(), LogType.ERROR))
        return Result.failure(e)
    } finally {
        finallyBlock()
    }
}

fun <T> DataViewModel.withErrorHandling(finallyBlock: () -> Unit = {}, block: () -> T): Result<T> =
    withErrorHandling(this.uiViewModel, finallyBlock, block)

@OptIn(ExperimentalUuidApi::class)
suspend fun <T> withErrorHandlingAsync(appLogger: AppLogger, finallyBlock: suspend () -> Unit = {}, block: suspend () -> T): Result<T> {
    try {
        return Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: LanguageException) {
        if (appLogger.getDebugConsole() == ConsoleType.NONE)
            appLogger.setDebugConsole(ConsoleType.POPUP)
        appLogger.appendLog(Log(e.toString() + e.origins?.toTraceString(), LogType.ERROR))
        return Result.failure(e)
    } catch (e: Exception) {
        if (appLogger.getDebugConsole() == ConsoleType.NONE)
            appLogger.setDebugConsole(ConsoleType.POPUP)
        appLogger.appendLog(Log(e.stackTraceToString(), LogType.ERROR))
        return Result.failure(e)
    } finally {
        finallyBlock()
    }
}
suspend fun <T> DataViewModel.withErrorHandlingAsync(finallyBlock: suspend () -> Unit = {}, block: suspend () -> T): Result<T> =
    withErrorHandlingAsync(this.uiViewModel, finallyBlock, block)

expect fun dumpFunctionsToFile(engine: CakeBakerEngine)

fun BigDecimal.roundTo1dp(): BigDecimal = this.roundSignificand(globalDecimalMode)