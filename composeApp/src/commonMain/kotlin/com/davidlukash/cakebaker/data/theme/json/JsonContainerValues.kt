package com.davidlukash.cakebaker.data.theme.json

import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.ContainerValues

data class JsonContainerValues(
    val elevation: Float? = null,
    val borderRadius: Float? = null,
    val borderWidth: Float? = null,
    val contentPadding: Float? = null,
) {
    fun toValues(base: ContainerValues): ContainerValues = base.copy(
        elevation = elevation?.dp ?: base.elevation,
        borderRadius = borderRadius?.dp ?: base.borderRadius,
        borderWidth = borderWidth?.dp ?: base.borderWidth,
        contentPadding = contentPadding?.dp ?: base.contentPadding,
    )
}
