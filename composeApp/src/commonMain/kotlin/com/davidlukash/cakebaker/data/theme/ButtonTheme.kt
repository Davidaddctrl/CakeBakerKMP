package com.davidlukash.cakebaker.data.theme

import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.theme.json.JsonButtonTheme

data class ButtonTheme(
    val containerColor: Color,
    val disabledContainerColor: Color,
    val contentColor: Color,
    val disabledContentColor: Color,
    val borderColor: Color,
    val disabledBorderColor: Color,
    val shouldDropShadow: Boolean,
) {
    fun toJsonTheme(): JsonButtonTheme = JsonButtonTheme(
        containerColor = containerColor,
        disabledContainerColor = disabledContainerColor,
        contentColor = contentColor,
        disabledContentColor = disabledContentColor,
        borderColor = borderColor,
        disabledBorderColor = disabledBorderColor,
        shouldDropShadow = shouldDropShadow
    )
}
