package com.davidlukash.cakebaker.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.LocalDoDropShadow
import com.davidlukash.cakebaker.data.theme.Theme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProgressBar(amount: Double, text: String?, modifier: Modifier = Modifier) {
    val amount by animateFloatAsState(
        targetValue = amount.toFloat(),
        animationSpec = tween(
            durationMillis = 100,
            easing = LinearEasing
        )
    )
    val theme = Theme.ProgressBarTheme
    val values = Theme.ProgressBarValues
    val elevation = values.elevation
    val borderWidth = values.borderWidth
    val shape = RoundedCornerShape(values.borderRadius)
    Box(
        modifier = modifier
            .shadow(elevation, shape)
            .clip(shape)
            .background(theme.backgroundColorBrushDescriptor.toBrush(), shape)
            .border(BorderStroke(borderWidth, theme.borderColorBrushDescriptor.toBrush()), shape)
            .defaultMinSize(minHeight = values.minHeight),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(theme.filledColorBrushDescriptor.toBrush(), shape)
                .fillMaxWidth(amount)
                .defaultMinSize(minHeight = values.minHeight)
                .align(Alignment.CenterStart)
        )
        if (text != null)
            CompositionLocalProvider(
                LocalDoDropShadow provides theme.shouldDropShadow
            ) {
                CompositionLocalProvider(
                    LocalTextStyle provides Theme.Styles.verySmallBodyStyle.copy(
                        brush = theme.contentColorBrushDescriptor.toBrush(),
                        fontWeight = if (theme.contentBold) FontWeight.Bold else Theme.Styles.verySmallBodyStyle.fontWeight,
                        textAlign = TextAlign.Center
                    )
                ) {
                    Text(text)
                }
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
    ProgressBar(amount = amount.toDouble(), text = amount.toString(), modifier = Modifier.width(320.dp))
}