package com.davidlukash.cakebaker.data.theme

import com.davidlukash.cakebaker.data.theme.json.BrushDescriptor
import com.davidlukash.cakebaker.data.theme.json.JsonButtonTheme

data class ButtonTheme(
    val containerColorBrushDescriptor: BrushDescriptor,
    val disabledContainerColorBrushDescriptor: BrushDescriptor,
    val contentColorBrushDescriptor: BrushDescriptor,
    val disabledContentColorBrushDescriptor: BrushDescriptor,
    val borderColorBrushDescriptor: BrushDescriptor,
    val disabledBorderColorBrushDescriptor: BrushDescriptor,
    val shouldDropShadow: Boolean,
) {
    fun toJsonTheme(): JsonButtonTheme = JsonButtonTheme(
        containerColorBrushDescriptor = containerColorBrushDescriptor,
        disabledContainerColorBrushDescriptor = disabledContainerColorBrushDescriptor,
        contentColorBrushDescriptor = contentColorBrushDescriptor,
        disabledContentColorBrushDescriptor = disabledContentColorBrushDescriptor,
        borderColorBrushDescriptor = borderColorBrushDescriptor,
        disabledBorderColorBrushDescriptor = disabledBorderColorBrushDescriptor,
        shouldDropShadow = shouldDropShadow
    )
}
