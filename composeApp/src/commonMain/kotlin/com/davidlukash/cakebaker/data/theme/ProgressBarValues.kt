package com.davidlukash.cakebaker.data.theme

import androidx.compose.ui.unit.Dp
import com.davidlukash.cakebaker.data.theme.json.JsonProgressBarValues

data class ProgressBarValues(
    val elevation: Dp,
    val borderRadius: Dp,
    val borderWidth: Dp,
    val minHeight: Dp
) {
    fun toJsonValues() = JsonProgressBarValues(
        elevation = elevation.value,
        borderRadius = borderRadius.value,
        borderWidth = borderWidth.value,
        minHeight = minHeight.value
    )
}
