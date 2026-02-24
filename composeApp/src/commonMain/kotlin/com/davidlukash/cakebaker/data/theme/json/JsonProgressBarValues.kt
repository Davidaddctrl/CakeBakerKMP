package com.davidlukash.cakebaker.data.theme.json

import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.ProgressBarValues
import kotlinx.serialization.Serializable

@Serializable
data class JsonProgressBarValues(
    val elevation: Float? = null,
    val borderRadius: Float? = null,
    val borderWidth: Float? = null,
    val minHeight: Float? = null
) {
    fun toValues(base: ProgressBarValues) = base.copy(
        elevation = elevation?.dp ?: base.elevation,
        borderRadius = borderRadius?.dp ?: base.borderRadius,
        borderWidth = borderWidth?.dp ?: base.borderWidth,
        minHeight = minHeight?.dp ?: base.minHeight,
    )
}
