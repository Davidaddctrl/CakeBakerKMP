package com.davidlukash.cakebaker.data.theme.json

import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.serializers.ColorSerializer
import com.davidlukash.cakebaker.data.theme.ButtonTheme
import kotlinx.serialization.Serializable

@Serializable
data class JsonButtonTheme(
    val containerColorBrushDescriptor: BrushDescriptor? = null,
    val disabledContainerColorBrushDescriptor: BrushDescriptor? = null,
    val contentColorBrushDescriptor: BrushDescriptor? = null,
    val disabledContentColorBrushDescriptor: BrushDescriptor? = null,
    val borderColorBrushDescriptor: BrushDescriptor? = null,
    val disabledBorderColorBrushDescriptor: BrushDescriptor? = null,
    val shouldDropShadow: Boolean? = null,
) {
    fun toTheme(base: ButtonTheme) = base.copy(
        containerColorBrushDescriptor = containerColorBrushDescriptor ?: base.containerColorBrushDescriptor,
        disabledContainerColorBrushDescriptor = disabledContainerColorBrushDescriptor ?: base.disabledContainerColorBrushDescriptor,
        contentColorBrushDescriptor = contentColorBrushDescriptor ?: base.contentColorBrushDescriptor,
        disabledContentColorBrushDescriptor = disabledContentColorBrushDescriptor ?: base.disabledContentColorBrushDescriptor,
        borderColorBrushDescriptor = borderColorBrushDescriptor ?: base.borderColorBrushDescriptor,
        disabledBorderColorBrushDescriptor = disabledBorderColorBrushDescriptor ?: base.disabledBorderColorBrushDescriptor,
        shouldDropShadow = shouldDropShadow ?: base.shouldDropShadow,
    )
}
