package com.davidlukash.cakebaker.data.theme.json

import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.client
import com.davidlukash.cakebaker.data.ImageData
import com.davidlukash.cakebaker.data.serializers.ColorSerializer
import com.davidlukash.cakebaker.data.theme.ContainerValues
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.loadBytesToFont
import com.davidlukash.cakebaker.withResultSuspend
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsBytes
import kotlinx.serialization.Serializable
import kotlin.io.encoding.Base64

@Serializable
data class JsonTheme(
    val idToImageMap: Map<String, String?> = mapOf(),
    val idToStringMap: Map<String, String> = mapOf(),
    val externalFont: String? = null,
    val scaledStyles: JsonTextStyles? = null,
    val unscaledStyles: JsonTextStyles? = null,
    val progressBarTheme: JsonProgressBarTheme? = null,
    val buttonTheme: JsonButtonTheme? = null,
    val switchButtonTheme: JsonSwitchButtonTheme? = null,
    val backgroundTheme: JsonContainerTheme? = null,
    val containerTheme: JsonContainerTheme? = null,
    val secondaryContainerTheme: JsonContainerTheme? = null,
    val textFieldTheme: JsonTextFieldTheme? = null,
    @Serializable(with = ColorSerializer::class)
    val successColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val dangerColor: Color? = null,
    @Serializable(with = ColorSerializer::class)
    val tabSelectedColor: Color? = null,
    val largeButtonValues: JsonButtonValues? = null,
    val smallButtonValues: JsonButtonValues? = null,
    val backgroundValues: JsonContainerValues? = null,
    val largePrimaryContainerValues: JsonContainerValues? = null,
    val largeSecondaryContainerValues: JsonContainerValues? = null,
    val smallPrimaryContainerValues: JsonContainerValues? = null,
    val smallSecondaryContainerValues: JsonContainerValues? = null,
) {
    suspend fun loadToTheme(base: Theme): Theme {
        val intersection = base.idToImageMap.keys intersect idToImageMap.keys
        val uniqueFromBase = base.idToImageMap.keys - idToImageMap.keys
        val uniqueFromBaseMap = uniqueFromBase.mapNotNull { key ->
            base.idToImageMap[key]?.let { key to it }
        }.toMap()
        val uniqueFromThis = idToImageMap.keys - base.idToImageMap.keys
        val uniqueFromThisMap = uniqueFromThis.associateWith { key ->
            ImageData(imagePath = idToImageMap[key])
        }
        val nonOverridden = uniqueFromBaseMap + uniqueFromThisMap
        val merged = intersection.mapNotNull { key ->
            val fromBase = base.idToImageMap[key]
            val fromThis = idToImageMap[key]
            fromBase?.copy(imagePath = fromThis)?.let { key to it }
        }.toMap()
        val total = nonOverridden + merged
        val fontResult = withResultSuspend {
            if (externalFont != null) {
                val bytes = if (externalFont.startsWith("http")) {
                    val response = client.get(externalFont)
                    response.bodyAsBytes()
                } else Base64.decode(externalFont)
                loadBytesToFont(externalFont.take(64), bytes)
            } else null
        }
        return base.copy(
            idToImageMap = total,
            idToStringMap = base.idToStringMap + idToStringMap,
            externalFont = fontResult.getOrNull() ?: base.externalFont,
            _scaledStyles = scaledStyles?.toTheme(base._scaledStyles) ?: base._scaledStyles,
            _unscaledStyles = unscaledStyles?.toTheme(base._unscaledStyles) ?: base._unscaledStyles,
            progressBarTheme = progressBarTheme?.toTheme(base.progressBarTheme) ?: base.progressBarTheme,
            buttonTheme = buttonTheme?.toTheme(base.buttonTheme) ?: base.buttonTheme,
            switchButtonTheme = switchButtonTheme?.toTheme(base.switchButtonTheme) ?: base.switchButtonTheme,
            backgroundTheme = backgroundTheme?.toTheme(base.backgroundTheme) ?: base.backgroundTheme,
            containerTheme = containerTheme?.toTheme(base.containerTheme) ?: base.containerTheme,
            secondaryContainerTheme = secondaryContainerTheme?.toTheme(base.secondaryContainerTheme)
                ?: base.secondaryContainerTheme,
            textFieldTheme = textFieldTheme?.toTheme(base.textFieldTheme) ?: base.textFieldTheme,
            successColor = successColor ?: base.successColor,
            dangerColor = dangerColor ?: base.dangerColor,
            tabSelectedColor = tabSelectedColor ?: base.tabSelectedColor,
            largeButtonValues = largeButtonValues?.toValues(base.largeButtonValues) ?: base.largeButtonValues,
            smallButtonValues = smallButtonValues?.toValues(base.smallButtonValues) ?: base.smallButtonValues,
            backgroundValues = backgroundValues?.toValues(base.backgroundValues) ?: base.backgroundValues,
            largePrimaryContainerValues = largePrimaryContainerValues?.toValues(base.largePrimaryContainerValues) ?: base.largePrimaryContainerValues,
            largeSecondaryContainerValues = largeSecondaryContainerValues?.toValues(base.largeSecondaryContainerValues) ?: base.largeSecondaryContainerValues,
            smallPrimaryContainerValues = smallPrimaryContainerValues?.toValues(base.smallPrimaryContainerValues) ?: base.smallPrimaryContainerValues,
            smallSecondaryContainerValues = smallSecondaryContainerValues?.toValues(base.smallSecondaryContainerValues) ?: base.smallSecondaryContainerValues,
        )
    }
}