package com.davidlukash.cakebaker.data.theme.json

import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.serializers.ColorSerializer
import com.davidlukash.cakebaker.data.theme.ButtonTheme
import kotlinx.serialization.Serializable

@Serializable
data class JsonButtonTheme(
    @Serializable(with = ColorSerializer::class)
    val containerColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val disabledContainerColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val contentColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val disabledContentColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val borderColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val disabledBorderColor: Color? = null,
    val shouldDropShadow: Boolean? = null,
) {
    fun toTheme(base: ButtonTheme) = base.copy(
        containerColor = containerColor ?: base.containerColor,
        disabledContainerColor = disabledContainerColor ?: base.disabledContainerColor,
        contentColor = contentColor ?: base.contentColor,
        disabledContentColor = disabledContentColor ?: base.disabledContentColor,
        borderColor = borderColor ?: base.borderColor,
        disabledBorderColor = disabledBorderColor ?: base.disabledBorderColor,
        shouldDropShadow = shouldDropShadow ?: base.shouldDropShadow,
    )
}
