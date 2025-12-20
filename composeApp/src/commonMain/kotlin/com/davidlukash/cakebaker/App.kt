package com.davidlukash.cakebaker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.davidlukash.cakebaker.ui.DebugMenu
import com.davidlukash.cakebaker.ui.GameTheme
import com.davidlukash.cakebaker.ui.MessageManager
import com.davidlukash.cakebaker.ui.ScaleViewport
import com.davidlukash.cakebaker.ui.navigation.Navigation
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.viewmodel.MainViewModel
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.floor
import kotlin.math.pow
import kotlin.random.Random

const val VERSION = "Alpha"

fun BigDecimal.toDouble(): Double {
    return ((this.roundToDigitPositionAfterDecimalPoint(2L, RoundingMode.FLOOR) * 100).longValue().toDouble() / 100)
}

fun BigDecimal.log10Int(): Int {
    return this.toString().split("+")[1].toInt()
}

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
        fun convert(value: Float): Float = if (value <= 0.03928f) value / 12.92f else ((value + 0.055f) / 1.055f).pow(2.4f)
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

const val usingDebugMenu = true

@Composable
@Preview
fun App() {
    val localMainViewModel = remember { MainViewModel() }
    CompositionLocalProvider(
        LocalMainViewModel provides localMainViewModel
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            val density = LocalDensity.current
            LaunchedEffect(density) {
                localMainViewModel.uiViewModel.updateTrueDensity(density)
            }
            Box(
                modifier = Modifier.weight(3f).fillMaxSize(),
            ) {
                GameTheme {
                    ScaleViewport(2000.dp, 1080.dp, doAspectRatio = false) {
                        Navigation()
                    }
                }
            }
            if (usingDebugMenu) DebugMenu()
        }
    }
}