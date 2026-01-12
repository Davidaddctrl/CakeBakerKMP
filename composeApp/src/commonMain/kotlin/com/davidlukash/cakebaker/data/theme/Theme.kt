package com.davidlukash.cakebaker.data.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.ResourceFont
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cakebaker.composeapp.generated.resources.Res
import cakebaker.composeapp.generated.resources.baking_powder
import cakebaker.composeapp.generated.resources.butter
import cakebaker.composeapp.generated.resources.choc_cake
import cakebaker.composeapp.generated.resources.cocoa_powder
import cakebaker.composeapp.generated.resources.egg
import cakebaker.composeapp.generated.resources.face_happy
import cakebaker.composeapp.generated.resources.face_medium
import cakebaker.composeapp.generated.resources.face_neutral
import cakebaker.composeapp.generated.resources.face_neutral_sad
import cakebaker.composeapp.generated.resources.face_sad
import cakebaker.composeapp.generated.resources.flour
import cakebaker.composeapp.generated.resources.honey_cake
import cakebaker.composeapp.generated.resources.honey_pot
import cakebaker.composeapp.generated.resources.ingredient_shop
import cakebaker.composeapp.generated.resources.money
import cakebaker.composeapp.generated.resources.oven
import cakebaker.composeapp.generated.resources.sugar
import cakebaker.composeapp.generated.resources.upgrade_shop
import cakebaker.composeapp.generated.resources.vanilla_cake
import cakebaker.composeapp.generated.resources.vanilla_extract
import cakebaker.composeapp.generated.resources.vcr_osd_mono
import com.davidlukash.cakebaker.data.ImageData
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.FontResource

@Composable
fun convertStyles(styles: TextStyles, font: FontResource): TextStyles {
    val fontFamily = FontFamily(Font(font))
    return styles.copy(
        titleStyle = convertStyle(styles.titleStyle, fontFamily),
        buttonTextStyle = convertStyle(styles.buttonTextStyle, fontFamily),
        largeBodyStyle = convertStyle(styles.largeBodyStyle, fontFamily),
        mediumBodyStyle = convertStyle(styles.mediumBodyStyle, fontFamily),
        smallBodyStyle = convertStyle(styles.smallBodyStyle, fontFamily),
        verySmallBodyStyle = convertStyle(styles.verySmallBodyStyle, fontFamily),
    )
}

@Composable
fun convertStyle(style: TextStyle, fontFamily: FontFamily): TextStyle {
    val density = LocalDensity.current
    val shadow = if (LocalDoDropShadow.current) style.shadow else null
    return style.copy(
        fontFamily = fontFamily,
        shadow = shadow?.copy(
            offset = density.run { shadow.offset.copy(x = shadow.offset.x.dp.toPx(), y = shadow.offset.y.dp.toPx()) }
        )
    )
}

data class Theme(
    val nameToImageMap: Map<String, ImageData>,
    val font: FontResource,
    val _scaledStyles: TextStyles,
    val _unscaledStyles: TextStyles,
    val progressBarTheme: ProgressBarTheme,
    val buttonTheme: ButtonTheme,
    val switchButtonTheme: SwitchButtonTheme,
    val backgroundTheme: ContainerTheme,
    val containerTheme: ContainerTheme,
    val secondaryContainerTheme: ContainerTheme,
    val successColor: Color,
    val dangerColor: Color,
) {

    fun nameToImage(name: String): ImageData {
        return nameToImageMap[name] ?: ImageData()
    }

    val scaledStyles: TextStyles
        @Composable
        get() = convertStyles(_scaledStyles, font)

    val unscaledStyles: TextStyles
        @Composable
        get() = convertStyles(_unscaledStyles, font)

    companion object {
        //Do not use this, use getDefaultTheme instead
        val default = Theme(
            nameToImageMap = mapOf(
                "Butter" to ImageData(resource = Res.drawable.butter),
                "Egg" to ImageData(resource = Res.drawable.egg),
                "Flour" to ImageData(resource = Res.drawable.flour),
                "Sugar" to ImageData(resource = Res.drawable.sugar),
                "Vanilla Extract" to ImageData(resource = Res.drawable.vanilla_extract),
                "Baking Powder" to ImageData(resource = Res.drawable.baking_powder),
                "Cocoa Powder" to ImageData(resource = Res.drawable.cocoa_powder),
                "Honey Pot" to ImageData(resource = Res.drawable.honey_pot),
                "Vanilla Cake" to ImageData(resource = Res.drawable.vanilla_cake),
                "Chocolate Cake" to ImageData(resource = Res.drawable.choc_cake),
                "Honey Cake" to ImageData(resource = Res.drawable.honey_cake),
                "Money" to ImageData(resource = Res.drawable.money),
                "Ingredient Shop" to ImageData(resource = Res.drawable.ingredient_shop),
                "Upgrade Shop" to ImageData(resource = Res.drawable.upgrade_shop),
                "Oven" to ImageData(resource = Res.drawable.oven),
                "Happy Face" to ImageData(resource = Res.drawable.face_happy),
                "Medium Face" to ImageData(resource = Res.drawable.face_medium),
                "Neutral Face" to ImageData(resource = Res.drawable.face_neutral),
                "Neutral Sad Face" to ImageData(resource = Res.drawable.face_neutral_sad),
                "Sad Face" to ImageData(resource = Res.drawable.face_sad),
            ),
            font = Res.font.vcr_osd_mono,
            _scaledStyles = TextStyles(
                titleStyle = TextStyle(fontSize = 72.sp, shadow = Shadow(color = Color.Black, offset = Offset(0f, 4f))),
                buttonTextStyle = TextStyle(fontSize = 60.sp, shadow = Shadow(color = Color.Black, offset = Offset(0f, 4f))),
                largeBodyStyle = TextStyle(fontSize = 48.sp, shadow = Shadow(color = Color.Black, offset = Offset(0f, 3f))),
                mediumBodyStyle = TextStyle(fontSize = 36.sp, shadow = Shadow(color = Color.Black, offset = Offset(0f, 3f))),
                smallBodyStyle = TextStyle(fontSize = 32.sp, shadow = Shadow(color = Color.Black, offset = Offset(0f, 3f))),
                verySmallBodyStyle = TextStyle(fontSize = 22.sp, shadow = Shadow(color = Color.Black, offset = Offset(0f, 2f))),
            ),
            _unscaledStyles = TextStyles(
                titleStyle = TextStyle(fontSize = 36.sp, shadow = Shadow(color = Color.Black, offset = Offset(0f, 2f))),
                buttonTextStyle = TextStyle(fontSize = 22.sp, shadow = Shadow(color = Color.Black, offset = Offset(0f, 2f))),
                largeBodyStyle = TextStyle(fontSize = 22.sp, shadow = Shadow(color = Color.Black, offset = Offset(0f, 2f))),
                mediumBodyStyle = TextStyle(fontSize = 12.sp, shadow = Shadow(color = Color.Black, offset = Offset(0f, 2f))),
                smallBodyStyle = TextStyle(fontSize = 12.sp, shadow = Shadow(color = Color.Black, offset = Offset(0f, 2f))),
                verySmallBodyStyle = TextStyle(fontSize = 12.sp, shadow = Shadow(color = Color.Black, offset = Offset(0f, 2f)))
            ),
            progressBarTheme = ProgressBarTheme(
                border = Color.Black,
                backgroundColor = Color(127, 127, 127),
                filledColor = Color(255, 127, 0)
            ),
            buttonTheme = ButtonTheme(
                containerColor = Color(8, 160, 69),
                disabledContainerColor = Color(37, 41, 46),
                contentColor = Color(255, 255, 255),
                disabledContentColor = Color(128, 128, 128),
                borderColor = Color(0, 0, 0),
                disabledBorderColor = Color(53, 57, 62),
            ),
            switchButtonTheme = SwitchButtonTheme(
                borderColor = Color.Black,
                offSelectedContainerColor = Color(255, 0, 0),
                offUnselectedContainerColor = Color(255, 0, 0).copy(alpha = 0.3f),
                offSelectedTextColor = Color(255, 255, 255),
                offUnselectedTextColor = Color(255, 255, 255).copy(alpha = 0.3f),
                onSelectedContainerColor = Color(58, 158, 0),
                onUnselectedContainerColor = Color(58, 158, 0).copy(alpha = 0.3f),
                onSelectedTextColor = Color(255, 255, 255),
                onUnselectedTextColor = Color(255, 255, 255).copy(alpha = 0.3f),
            ),
            backgroundTheme = ContainerTheme(
                borderColor = Color.Transparent,
                containerColor = Color(0, 120, 255),
                contentColor = Color(255, 255, 255),
            ),
            containerTheme = ContainerTheme(
                borderColor = Color(0, 0, 0),
                containerColor = Color(246, 255, 153),
                contentColor = Color(0, 0, 0),
                shouldDropShadow = false
            ),
            secondaryContainerTheme = ContainerTheme(
                borderColor = Color(0, 0, 0),
                containerColor = Color(8, 160, 69),
                contentColor = Color(255, 255, 255),
            ),
            successColor = Color(58, 158, 0),
            dangerColor = Color(255, 0, 0)
        )
    }
}

val LocalDoDropShadow = compositionLocalOf { true }

val LocalTheme = compositionLocalOf { Theme.default }