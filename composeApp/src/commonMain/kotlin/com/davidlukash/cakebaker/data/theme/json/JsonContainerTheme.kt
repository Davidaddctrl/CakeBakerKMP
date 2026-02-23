package com.davidlukash.cakebaker.data.theme.json

import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.serializers.ColorSerializer
import com.davidlukash.cakebaker.data.theme.ContainerTheme
import kotlinx.serialization.Serializable

@Serializable
data class JsonContainerTheme(
    val borderColorBrushDescriptor: BrushDescriptor? = null,
    val containerColorBrushDescriptor: BrushDescriptor? = null,
    @Serializable(with = ColorSerializer::class)
    val contentColor: Color? = null,
    val shouldDropShadow: Boolean? = null
) {
    fun toTheme(base: ContainerTheme): ContainerTheme = base.copy(
        borderColorBrushDescriptor = borderColorBrushDescriptor ?: base.borderColorBrushDescriptor,
        containerColorBrushDescriptor = containerColorBrushDescriptor ?: base.containerColorBrushDescriptor,
        contentColor = contentColor ?: base.contentColor,
        shouldDropShadow = shouldDropShadow ?: base.shouldDropShadow
    )
}