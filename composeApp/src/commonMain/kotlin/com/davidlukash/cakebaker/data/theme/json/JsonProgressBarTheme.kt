package com.davidlukash.cakebaker.data.theme.json

import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.serializers.ColorSerializer
import com.davidlukash.cakebaker.data.theme.ProgressBarTheme
import kotlinx.serialization.Serializable

@Serializable
data class JsonProgressBarTheme(
    @Serializable(with = ColorSerializer::class)
    val border: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val backgroundColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val filledColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val contentColor: Color? = null,
) {
    fun toTheme(base: ProgressBarTheme) = base.copy(
        border = border ?: base.border,
        backgroundColor = backgroundColor ?: base.backgroundColor,
        filledColor = filledColor ?: base.filledColor,
        contentColor = contentColor ?: base.contentColor,
    )
}