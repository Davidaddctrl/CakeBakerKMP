package com.davidlukash.cakebaker.data.theme.json

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.serializers.ColorSerializer
import kotlinx.serialization.Serializable

@Serializable
sealed class BrushDescriptor {
    @Serializable
    class SolidColor(@Serializable(with = ColorSerializer::class) val color: Color) : BrushDescriptor()
    @Serializable
    class VerticalGradient(val colors: List<@Serializable(with = ColorSerializer::class) Color>) : BrushDescriptor()
    @Serializable
    class HorizontalGradient(val colors: List<@Serializable(with = ColorSerializer::class) Color>) : BrushDescriptor()
    fun toBrush(): Brush = when(this) {
        is VerticalGradient -> Brush.verticalGradient(colors)
        is HorizontalGradient -> Brush.horizontalGradient(colors)
        is SolidColor -> androidx.compose.ui.graphics.SolidColor(color)
    }

    fun toColor(): Color = when(this) {
        is VerticalGradient -> colors.firstOrNull() ?: Color.Unspecified
        is HorizontalGradient -> colors.firstOrNull() ?: Color.Unspecified
        is SolidColor -> color
    }
}