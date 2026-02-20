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
    class LinearGradient(val colors: List<@Serializable(with = ColorSerializer::class) Color>) : BrushDescriptor()
    fun toBrush(): Brush = when(this) {
        is LinearGradient -> Brush.linearGradient(colors)
        is SolidColor -> androidx.compose.ui.graphics.SolidColor(color)
    }
}