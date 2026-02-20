package com.davidlukash.cakebaker.data.theme.json

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.serializers.ColorSerializer
import com.davidlukash.cakebaker.data.theme.TextFieldTheme
import kotlinx.serialization.Serializable

@Serializable
data class JsonTextFieldTheme(
    val cursorBrushDescriptor: BrushDescriptor? = null,
    @Serializable(with = ColorSerializer::class)
    val contentColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val placeholderColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val containerColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val borderColor: Color? = null,
    val shouldDropShadow: Boolean? = null
) {
    fun toTheme(base: TextFieldTheme): TextFieldTheme = base.copy(
        cursorBrush = cursorBrushDescriptor?.toBrush() ?: base.cursorBrush,
        cursorBrushDescriptor = cursorBrushDescriptor ?: base.cursorBrushDescriptor,
        contentColor = contentColor ?: base.contentColor,
        placeholderColor = placeholderColor ?: base.placeholderColor,
        containerColor = containerColor ?: base.containerColor,
        borderColor = borderColor ?: base.borderColor,
        shouldDropShadow = shouldDropShadow ?: base.shouldDropShadow
    )
}
