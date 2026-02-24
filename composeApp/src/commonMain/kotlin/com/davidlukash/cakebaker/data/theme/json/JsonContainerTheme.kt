package com.davidlukash.cakebaker.data.theme.json

import com.davidlukash.cakebaker.data.theme.ContainerTheme
import kotlinx.serialization.Serializable

@Serializable
data class JsonContainerTheme(
    val borderColorBrushDescriptor: BrushDescriptor? = null,
    val containerColorBrushDescriptor: BrushDescriptor? = null,
    val contentColorBrushDescriptor: BrushDescriptor? = null,
    val shouldDropShadow: Boolean? = null
) {
    fun toTheme(base: ContainerTheme): ContainerTheme = base.copy(
        borderColorBrushDescriptor = borderColorBrushDescriptor ?: base.borderColorBrushDescriptor,
        containerColorBrushDescriptor = containerColorBrushDescriptor ?: base.containerColorBrushDescriptor,
        contentColorBrushDescriptor = contentColorBrushDescriptor ?: base.contentColorBrushDescriptor,
        shouldDropShadow = shouldDropShadow ?: base.shouldDropShadow
    )
}