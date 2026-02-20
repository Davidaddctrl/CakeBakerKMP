package com.davidlukash.cakebaker.data.theme.json

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.davidlukash.cakebaker.data.serializers.ColorSerializer
import kotlinx.serialization.Serializable

@Serializable
data class JsonTextStyle(
    val fontSize: Float,
    @Serializable(with = ColorSerializer::class)
    val shadowColor: Color? = null,
    val shadowOffsetX: Float? = null,
    val shadowOffsetY: Float? = null,
) {
    fun toTextStyle(): TextStyle = TextStyle(
        fontSize = fontSize.sp,
        shadow = Shadow(
            color = shadowColor ?: Color.Unspecified,
            offset = Offset(shadowOffsetX ?: 0f, shadowOffsetY ?: 0f),
        )
    )

    companion object {
        fun fromTextStyle(textStyle: TextStyle): JsonTextStyle = JsonTextStyle(
            fontSize = textStyle.fontSize.value,
            shadowColor = textStyle.shadow?.color,
            shadowOffsetX = textStyle.shadow?.offset?.x,
            shadowOffsetY = textStyle.shadow?.offset?.y,
        )
    }
}
