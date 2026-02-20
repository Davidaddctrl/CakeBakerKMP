package com.davidlukash.cakebaker.data.theme.json

import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.serializers.ColorSerializer
import com.davidlukash.cakebaker.data.theme.SwitchButtonTheme
import kotlinx.serialization.Serializable

@Serializable
data class JsonSwitchButtonTheme(
    @Serializable(with = ColorSerializer::class)
    val borderColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val disabledBorderColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val containerColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val disabledContainerColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val offSelectedContainerColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val offUnselectedContainerColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val offSelectedTextColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val offUnselectedTextColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val onSelectedContainerColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val onUnselectedContainerColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val onSelectedTextColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val onUnselectedTextColor: Color? = null,
    val shouldDropShadow: Boolean? = null,
) {
    fun toTheme(base: SwitchButtonTheme) = base.copy(
        borderColor = borderColor ?: base.borderColor,
        disabledBorderColor = disabledBorderColor ?: base.disabledBorderColor,
        containerColor = containerColor ?: base.containerColor,
        disabledContainerColor = disabledContainerColor ?: base.disabledContainerColor,
        offSelectedContainerColor = offSelectedContainerColor ?: base.offSelectedContainerColor,
        offUnselectedContainerColor = offUnselectedContainerColor ?: base.offUnselectedContainerColor,
        offSelectedTextColor = offSelectedTextColor ?: base.offSelectedTextColor,
        offUnselectedTextColor = offUnselectedTextColor ?: base.offUnselectedTextColor,
        onSelectedContainerColor = onSelectedContainerColor ?: base.onSelectedContainerColor,
        onUnselectedContainerColor = onUnselectedContainerColor ?: base.onUnselectedContainerColor,
        onSelectedTextColor = onSelectedTextColor ?: base.onSelectedTextColor,
        onUnselectedTextColor = onUnselectedTextColor ?: base.onUnselectedTextColor,
        shouldDropShadow = shouldDropShadow ?: base.shouldDropShadow,
    )
}
