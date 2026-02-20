package com.davidlukash.cakebaker.data.serializers

import androidx.compose.ui.text.TextStyle
import com.davidlukash.cakebaker.data.theme.json.JsonTextStyle
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class TextStyleSerializer : KSerializer<TextStyle> {
    private val baseSerializer = JsonTextStyle.serializer()

    override val descriptor = baseSerializer.descriptor

    override fun deserialize(decoder: Decoder): TextStyle = baseSerializer.deserialize(decoder).toTextStyle()

    override fun serialize(encoder: Encoder, value: TextStyle) {
        baseSerializer.serialize(encoder, JsonTextStyle.fromTextStyle(value))
    }
}