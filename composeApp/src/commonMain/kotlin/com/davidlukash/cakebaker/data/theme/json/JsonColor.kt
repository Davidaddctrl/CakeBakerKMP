package com.davidlukash.cakebaker.data.theme.json

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import kotlinx.serialization.Serializable

@Serializable
data class JsonColor(
    val red: Float,
    val green: Float,
    val blue: Float,
    val alpha: Float,
    val specified: Boolean
) {
    fun toColor(): Color = if (specified) Color(red, green, blue, alpha) else Color.Unspecified

    companion object {
        fun fromColor(color: Color): JsonColor = JsonColor(color.red, color.green, color.blue, color.alpha, color.isSpecified)
    }
}
