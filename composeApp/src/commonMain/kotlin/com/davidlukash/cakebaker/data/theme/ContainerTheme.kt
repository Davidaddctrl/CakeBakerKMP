package com.davidlukash.cakebaker.data.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.davidlukash.cakebaker.data.theme.json.BrushDescriptor
import com.davidlukash.cakebaker.data.theme.json.JsonContainerTheme

data class ContainerTheme(
    val borderColorBrushDescriptor: BrushDescriptor,
    val containerColorBrushDescriptor: BrushDescriptor,
    val contentColorBrushDescriptor: BrushDescriptor,
    val shouldDropShadow: Boolean
) {
    fun toJsonTheme(): JsonContainerTheme = JsonContainerTheme(
            borderColorBrushDescriptor = borderColorBrushDescriptor,
            containerColorBrushDescriptor = containerColorBrushDescriptor,
            contentColorBrushDescriptor = contentColorBrushDescriptor,
            shouldDropShadow = shouldDropShadow
        )
}

@Composable
fun ProvideContainer(containerTheme: ContainerTheme, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalDoDropShadow provides containerTheme.shouldDropShadow,
        LocalContentColor provides containerTheme.contentColorBrushDescriptor.toColor(),
        LocalTextStyle provides LocalTextStyle.current.copy(brush = containerTheme.contentColorBrushDescriptor.toBrush())
    ) {
        content()
    }
}