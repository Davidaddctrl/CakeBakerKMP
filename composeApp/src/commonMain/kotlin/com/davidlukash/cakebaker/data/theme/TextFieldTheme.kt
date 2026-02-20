package com.davidlukash.cakebaker.data.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.theme.json.BrushDescriptor
import com.davidlukash.cakebaker.data.theme.json.JsonTextFieldTheme

data class TextFieldTheme(
    val cursorBrush: Brush,
    val cursorBrushDescriptor: BrushDescriptor,
    val contentColor: Color,
    val placeholderColor: Color,
    val containerColor: Color,
    val borderColor: Color,
    val shouldDropShadow: Boolean
) {
    fun toJsonTheme() = JsonTextFieldTheme(
        cursorBrushDescriptor = cursorBrushDescriptor,
        contentColor = contentColor,
        placeholderColor = placeholderColor,
        containerColor = containerColor,
        borderColor = borderColor,
        shouldDropShadow = shouldDropShadow
    )
}
