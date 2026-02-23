package com.davidlukash.cakebaker.data.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.theme.json.BrushDescriptor
import com.davidlukash.cakebaker.data.theme.json.JsonContainerTheme

data class ContainerTheme(
    val borderColorBrushDescriptor: BrushDescriptor,
    val containerColorBrushDescriptor: BrushDescriptor,
    val contentColor: Color,
    val shouldDropShadow: Boolean
) {
    fun toJsonTheme(): JsonContainerTheme = JsonContainerTheme(
            borderColorBrushDescriptor = borderColorBrushDescriptor,
            containerColorBrushDescriptor = containerColorBrushDescriptor,
            contentColor = contentColor,
            shouldDropShadow = shouldDropShadow
        )
}

@Composable
fun ProvideContainer(containerTheme: ContainerTheme, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalDoDropShadow provides containerTheme.shouldDropShadow,
        LocalContentColor provides containerTheme.contentColor
    ) {
        content()
    }
}