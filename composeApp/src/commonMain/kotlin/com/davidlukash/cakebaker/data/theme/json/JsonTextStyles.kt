package com.davidlukash.cakebaker.data.theme.json

import androidx.compose.ui.text.TextStyle
import com.davidlukash.cakebaker.data.serializers.TextStyleSerializer
import com.davidlukash.cakebaker.data.theme.TextStyles
import kotlinx.serialization.Serializable

@Serializable
data class JsonTextStyles(
    @Serializable(with = TextStyleSerializer::class)
    val titleStyle: TextStyle? = null,
    @Serializable(with = TextStyleSerializer::class)
    val buttonTextStyle: TextStyle? = null,
    @Serializable(with = TextStyleSerializer::class)
    val largeBodyStyle: TextStyle? = null,
    @Serializable(with = TextStyleSerializer::class)
    val mediumBodyStyle: TextStyle? = null,
    @Serializable(with = TextStyleSerializer::class)
    val smallBodyStyle: TextStyle? = null,
    @Serializable(with = TextStyleSerializer::class)
    val verySmallBodyStyle: TextStyle? = null,
) {
    fun toTheme(base: TextStyles) = base.copy(
        titleStyle = titleStyle ?: base.titleStyle,
        buttonTextStyle = buttonTextStyle ?: base.buttonTextStyle,
        largeBodyStyle = largeBodyStyle ?: base.largeBodyStyle,
        mediumBodyStyle = mediumBodyStyle ?: base.mediumBodyStyle,
        smallBodyStyle = smallBodyStyle ?: base.smallBodyStyle,
        verySmallBodyStyle = verySmallBodyStyle ?: base.verySmallBodyStyle,
    )
}
