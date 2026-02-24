package com.davidlukash.cakebaker.data.theme.json

import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.serializers.ColorSerializer
import com.davidlukash.cakebaker.data.theme.ProgressBarTheme
import kotlinx.serialization.Serializable

@Serializable
data class JsonProgressBarTheme(
    val borderColorBrushDescriptor: BrushDescriptor? = null,
    val backgroundColorBrushDescriptor: BrushDescriptor? = null,
    val filledColorBrushDescriptor: BrushDescriptor? = null,
    val contentColorBrushDescriptor: BrushDescriptor? = null,
    val contentBold: Boolean? = null,
    val shouldDropShadow: Boolean? = null
) {
    fun toTheme(base: ProgressBarTheme) = base.copy(
        borderColorBrushDescriptor = borderColorBrushDescriptor ?: base.borderColorBrushDescriptor,
        backgroundColorBrushDescriptor = backgroundColorBrushDescriptor ?: base.backgroundColorBrushDescriptor,
        filledColorBrushDescriptor = filledColorBrushDescriptor ?: base.filledColorBrushDescriptor,
        contentColorBrushDescriptor = contentColorBrushDescriptor ?: base.contentColorBrushDescriptor,
        contentBold = contentBold ?: base.contentBold,
        shouldDropShadow = shouldDropShadow ?: base.shouldDropShadow
    )
}