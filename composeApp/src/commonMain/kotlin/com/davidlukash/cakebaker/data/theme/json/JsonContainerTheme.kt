package com.davidlukash.cakebaker.data.theme.json

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.serializers.ColorSerializer
import com.davidlukash.cakebaker.data.theme.ContainerTheme
import com.davidlukash.cakebaker.data.theme.LocalDoDropShadow
import kotlinx.serialization.Serializable

@Serializable
data class JsonContainerTheme(
    val borderColorBrushDescriptor: BrushDescriptor? = null,
    val containerColorBrushDescriptor: BrushDescriptor? = null,
    @Serializable(with = ColorSerializer::class)
    val contentColor: Color? = null,
    val shouldDropShadow: Boolean? = null
) {
    fun toTheme(base: ContainerTheme): ContainerTheme = base.copy(
        borderColorBrush = borderColorBrushDescriptor?.toBrush() ?: base.borderColorBrush,
        borderColorBrushDescriptor = borderColorBrushDescriptor ?: base.borderColorBrushDescriptor,
        containerColorBrush = containerColorBrushDescriptor?.toBrush() ?: base.containerColorBrush,
        containerColorBrushDescriptor = containerColorBrushDescriptor ?: base.containerColorBrushDescriptor,
        contentColor = contentColor ?: base.contentColor,
        shouldDropShadow = shouldDropShadow ?: base.shouldDropShadow
    )
}