package com.davidlukash.cakebaker

import androidx.compose.ui.graphics.Color
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlin.compareTo
import kotlin.math.floor
import kotlin.math.pow
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.toString

fun BigDecimal.toDouble(): Double {
    return ((this.roundToDigitPositionAfterDecimalPoint(2L, RoundingMode.FLOOR) * 100).longValue().toDouble() / 100)
}

fun BigDecimal.log10Int(): Int {
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

fun toEngNotation(number: BigDecimal): String {
    if (number < 1000) {
        return number.toDouble().toString().let {
            if (it.endsWith(".0")) {
                it.removeSuffix(".0")
            } else it
        }
    }
    val suffixes = listOf("", "K", "M", "B", "T", "Qa", "Qt", "Sx", "Sp", "Oc", "No")
    val index = floor(number.log10Int() / 3.0).toInt()
    val scaled = (number.divide(
        1000.0.pow(index).toBigDecimal(), decimalMode = DecimalMode(4, roundingMode = RoundingMode.FLOOR)
    )).roundToDigitPosition(4)
        .toDouble()
    val string = if (scaled % 1.0 == 0.0) {
        scaled.toString()
    } else {
        val rounded = floor(scaled * 10) / 10
        rounded.toString()
    }.let {
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
}
