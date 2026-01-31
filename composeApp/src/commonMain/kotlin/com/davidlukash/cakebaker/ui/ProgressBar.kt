package com.davidlukash.cakebaker.ui


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.ui.Box
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProgressBar(modifier: Modifier = Modifier, amount: Double) {
    key(amount) {
        Box(
            modifier = modifier
                .size(320.dp, 48.dp)
                .background(Theme.ProgressBarTheme.backgroundColor, 24.dp)
                .border(Theme.ProgressBarTheme.border, 8.dp, 24.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(amount.toFloat())
                    .height(32.dp)
                    .align(Alignment.CenterStart)
                    .background(Theme.ProgressBarTheme.filledColor, 24.dp)
            ) {}
        }
    }
}

@Composable
fun ProgressBar(modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier, amount: Double) {
    ProgressBar(
        modifier = Modifier.nativeComposeModifier(modifier),
        amount = amount
    )
}

@Preview
@Composable
fun ProgressBarPreview() {
    val infiniteTransition = rememberInfiniteTransition()
    val amount by infiniteTransition.animateFloat(
        0f, 1f, animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
        )
    )
    ProgressBar(amount = amount.toDouble(), modifier = Modifier)
}