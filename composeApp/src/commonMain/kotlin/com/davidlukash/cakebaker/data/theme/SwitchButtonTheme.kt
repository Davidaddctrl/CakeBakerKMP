package com.davidlukash.cakebaker.data.theme

import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.theme.json.JsonSwitchButtonTheme

data class SwitchButtonTheme(
    val borderColor: Color,
    val disabledBorderColor: Color,
    val containerColor: Color,
    val disabledContainerColor: Color,
    val offSelectedContainerColor: Color,
    val offUnselectedContainerColor: Color,
    val offSelectedTextColor: Color,
    val offUnselectedTextColor: Color,
    val onSelectedContainerColor: Color,
    val onUnselectedContainerColor: Color,
    val onSelectedTextColor: Color,
    val onUnselectedTextColor: Color,
    val shouldDropShadow: Boolean,
) {
    fun toJsonTheme() = JsonSwitchButtonTheme(
        borderColor = borderColor,
        disabledBorderColor = disabledBorderColor,
        containerColor = containerColor,
        disabledContainerColor = disabledContainerColor,
        offSelectedContainerColor = offSelectedContainerColor,
        offUnselectedContainerColor = offUnselectedContainerColor,
        offSelectedTextColor = offSelectedTextColor,
        offUnselectedTextColor = offUnselectedTextColor,
        onSelectedContainerColor = onSelectedContainerColor,
        onUnselectedContainerColor = onUnselectedContainerColor,
        onSelectedTextColor = onSelectedTextColor,
        onUnselectedTextColor = onUnselectedTextColor,
        shouldDropShadow = shouldDropShadow,
    )
}
