package com.davidlukash.cakebaker.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProgressBar(modifier: Modifier = Modifier, amount: Double) {
    Surface(
        modifier = modifier.width(320.dp).height(48.dp),
        shape = CircleShape,
        border = BorderStroke(8.dp, Theme.ProgressBarTheme.border),
        color = Theme.ProgressBarTheme.backgroundColor
    ) {
        Box {
            Surface(
                modifier = Modifier.fillMaxWidth(amount.toFloat()).height(48.dp),
                shape = CircleShape,
                color = Theme.ProgressBarTheme.filledColor
            ) {}
        }
    }
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
    ProgressBar(amount = amount.toDouble())
}