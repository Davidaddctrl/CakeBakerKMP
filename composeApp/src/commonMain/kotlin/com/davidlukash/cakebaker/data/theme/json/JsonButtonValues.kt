package com.davidlukash.cakebaker.data.theme.json

import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.ButtonValues
import kotlinx.serialization.Serializable

@Serializable
data class JsonButtonValues(
    val defaultElevation: Float? = null,
    val pressedElevation: Float? = null,
    val focusedElevation: Float? = null,
    val hoveredElevation: Float? = null,
    val disabledElevation: Float? = null,
    val borderRadius: Float? = null,
    val borderWidth: Float? = null,
    val contentPadding: Float? = null,
) {
    fun toValues(base: ButtonValues): ButtonValues = base.copy(
        defaultElevation = defaultElevation?.dp ?: base.defaultElevation,
        pressedElevation = pressedElevation?.dp ?: base.pressedElevation,
        focusedElevation = focusedElevation?.dp ?: base.focusedElevation,
        hoveredElevation = hoveredElevation?.dp ?: base.hoveredElevation,
        disabledElevation = disabledElevation?.dp ?: base.disabledElevation,
        borderRadius = borderRadius?.dp ?: base.borderRadius,
        borderWidth = borderWidth?.dp ?: base.borderWidth,
        contentPadding = contentPadding?.dp ?: base.contentPadding,
    )
}
