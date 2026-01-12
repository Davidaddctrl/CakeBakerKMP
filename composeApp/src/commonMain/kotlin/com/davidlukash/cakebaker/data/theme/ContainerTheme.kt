package com.davidlukash.cakebaker.data.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

data class ContainerTheme(
    val borderColor: Color,
    val containerColor: Color,
    val contentColor: Color,
    val shouldDropShadow: Boolean = true
)

@Composable
fun ProvideContainer(containerTheme: ContainerTheme, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalDoDropShadow provides containerTheme.shouldDropShadow,
        LocalContentColor provides containerTheme.contentColor
    ) {
        content()
    }
}