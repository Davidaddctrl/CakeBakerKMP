package com.davidlukash.cakebaker.data.serializers

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import com.davidlukash.cakebaker.data.theme.json.JsonColor
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class ColorSerializer : KSerializer<Color> {
    private val baseSerializer = JsonColor.serializer()
    override val descriptor: SerialDescriptor = baseSerializer.descriptor

    override fun deserialize(decoder: Decoder): Color {
        return baseSerializer.deserialize(decoder).toColor()
    }

    override fun serialize(
        encoder: Encoder,
        value: Color
    ) {
        baseSerializer.serialize(encoder, JsonColor.fromColor(value))
    }
}