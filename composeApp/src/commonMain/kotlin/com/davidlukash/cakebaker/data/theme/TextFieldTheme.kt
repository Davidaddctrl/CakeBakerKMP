package com.davidlukash.cakebaker.data.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class TextFieldTheme(
    val cursorBrush: Brush,
    val contentColor: Color,
    val placeholderColor: Color,
    val containerColor: Color,
    val borderColor: Color,
    val shouldDropShadow: Boolean
)
