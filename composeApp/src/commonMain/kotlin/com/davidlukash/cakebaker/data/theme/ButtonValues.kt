package com.davidlukash.cakebaker.data.theme

import androidx.compose.ui.unit.Dp
import com.davidlukash.cakebaker.data.theme.json.JsonButtonValues

data class ButtonValues(
    val defaultElevation: Dp,
    val pressedElevation: Dp,
    val focusedElevation: Dp,
    val hoveredElevation: Dp,
    val disabledElevation: Dp,
    val borderRadius: Dp,
    val borderWidth: Dp,
    val contentPadding: Dp,
) {
    fun toJsonValues(): JsonButtonValues = JsonButtonValues(
        defaultElevation = defaultElevation.value,
        pressedElevation = pressedElevation.value,
        focusedElevation = focusedElevation.value,
        hoveredElevation = hoveredElevation.value,
        disabledElevation = disabledElevation.value,
        borderRadius = borderRadius.value,
        borderWidth = borderWidth.value,
        contentPadding = contentPadding.value,
    )
}
