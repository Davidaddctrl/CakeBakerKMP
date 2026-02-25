package com.davidlukash.cakebaker.data.theme

import com.davidlukash.cakebaker.data.theme.json.BrushDescriptor
import com.davidlukash.cakebaker.data.theme.json.JsonProgressBarTheme

data class ProgressBarTheme(
    val borderColorBrushDescriptor: BrushDescriptor,
    val backgroundColorBrushDescriptor: BrushDescriptor,
    val filledColorBrushDescriptor: BrushDescriptor,
    val contentColorBrushDescriptor: BrushDescriptor,
    val contentBold: Boolean,
    val shouldDropShadow: Boolean
) {
    fun toJsonTheme(): JsonProgressBarTheme = JsonProgressBarTheme(
        borderColorBrushDescriptor = borderColorBrushDescriptor,
        backgroundColorBrushDescriptor = backgroundColorBrushDescriptor,
        filledColorBrushDescriptor = filledColorBrushDescriptor,
        contentColorBrushDescriptor = contentColorBrushDescriptor,
        contentBold = contentBold,
        shouldDropShadow = shouldDropShadow
    )
}