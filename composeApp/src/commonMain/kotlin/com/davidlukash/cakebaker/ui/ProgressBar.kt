package com.davidlukash.cakebaker.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProgressBar(theme: Theme, amount: Double) {
    Surface(
        modifier = Modifier.width(320.dp).height(48.dp),
        shape = CircleShape,
        border = BorderStroke(8.dp, theme.progressBarTheme.border),
        color = theme.progressBarTheme.backgroundColor
    ) {
        Box {
            Surface(
                modifier = Modifier.width((320 * amount).dp).height(48.dp),
                shape = CircleShape,
                color = theme.progressBarTheme.filledColor
            ) {}
        }
    }
}

@Preview
@Composable
fun ProgressBarPreview() {
    val theme = getDefaultTheme()
    val infiniteTransition = rememberInfiniteTransition()
    val amount by infiniteTransition.animateFloat(
        0f, 1f, animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
        )
    )
    ProgressBar(theme, amount.toDouble())
}