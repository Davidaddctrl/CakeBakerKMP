package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
@Composable
fun ScaleViewport(baseWidth: Dp, baseHeight: Dp, doAspectRatio: Boolean = true, content: @Composable () -> Unit) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val scaleX = maxWidth / baseWidth
        val scaleY = maxHeight / baseHeight
        val scale = minOf(scaleX, scaleY)
        val density = LocalDensity.current
        CompositionLocalProvider(
            //We set font scale to 1f as the game needs to have correct font sizes
            LocalDensity provides Density(density.density * scale, 1f),
        ) {
            Box(
                modifier = Modifier.align(Alignment.Center).then(
                    if (doAspectRatio) Modifier.aspectRatio(baseWidth / baseHeight) else Modifier.fillMaxSize()
                )
            ) {
                content()
            }
        }
    }
}