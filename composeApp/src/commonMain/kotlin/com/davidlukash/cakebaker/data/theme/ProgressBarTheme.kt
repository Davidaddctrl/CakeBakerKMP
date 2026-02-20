package com.davidlukash.cakebaker.data.theme

import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.theme.json.JsonProgressBarTheme

data class ProgressBarTheme(
    val border: Color,
    val backgroundColor: Color,
    val filledColor: Color,
    val contentColor: Color
) {
    fun toJsonTheme(): JsonProgressBarTheme = JsonProgressBarTheme(
        border = border,
        backgroundColor = backgroundColor,
        filledColor = filledColor,
        contentColor = contentColor
    )
}