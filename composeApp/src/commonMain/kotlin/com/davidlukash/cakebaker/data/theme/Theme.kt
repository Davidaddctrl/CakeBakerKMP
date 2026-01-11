package com.davidlukash.cakebaker.data.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
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

data class Theme(
    val nameToImageMap: Map<String, ImageData>,
    val font: FontFamily,
    val scaledStyles: TextStyles,
    val unscaledStyles: TextStyles,
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
            font = FontFamily.Default,
            scaledStyles = TextStyles(
                titleStyle = TextStyle(fontSize = 72.sp),
                buttonTextStyle = TextStyle(fontSize = 60.sp),
                largeBodyStyle = TextStyle(fontSize = 48.sp),
                mediumBodyStyle = TextStyle(fontSize = 36.sp),
                smallBodyStyle = TextStyle(fontSize = 32.sp),
                verySmallBodyStyle = TextStyle(fontSize = 22.sp),
            ),
            unscaledStyles = TextStyles(
                titleStyle = TextStyle(fontSize = 36.sp),
                buttonTextStyle = TextStyle(fontSize = 22.sp),
                largeBodyStyle = TextStyle(fontSize = 22.sp),
                mediumBodyStyle = TextStyle(fontSize = 12.sp),
                smallBodyStyle = TextStyle(fontSize = 12.sp),
                verySmallBodyStyle = TextStyle(fontSize = 12.sp)
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
                contentColor = Color(0, 0, 0)
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

@Composable
fun getDefaultTheme(): Theme {
    val theme = Theme.default
    val fontFamily = FontFamily(Font(Res.font.vcr_osd_mono))
    return theme.copy(
        font = fontFamily,
        scaledStyles = TextStyles(
            titleStyle = theme.scaledStyles.titleStyle.copy(fontFamily = fontFamily),
            largeBodyStyle = theme.scaledStyles.largeBodyStyle.copy(fontFamily = fontFamily),
            mediumBodyStyle = theme.scaledStyles.mediumBodyStyle.copy(fontFamily = fontFamily),
            smallBodyStyle = theme.scaledStyles.smallBodyStyle.copy(fontFamily = fontFamily),
            verySmallBodyStyle = theme.scaledStyles.verySmallBodyStyle.copy(fontFamily = fontFamily),
            buttonTextStyle = theme.scaledStyles.buttonTextStyle.copy(fontFamily = fontFamily),
        ),
        unscaledStyles = TextStyles(
            titleStyle = theme.unscaledStyles.titleStyle.copy(fontFamily = fontFamily),
            largeBodyStyle = theme.unscaledStyles.largeBodyStyle.copy(fontFamily = fontFamily),
            mediumBodyStyle = theme.unscaledStyles.mediumBodyStyle.copy(fontFamily = fontFamily),
            smallBodyStyle = theme.unscaledStyles.smallBodyStyle.copy(fontFamily = fontFamily),
            verySmallBodyStyle = theme.unscaledStyles.verySmallBodyStyle.copy(fontFamily = fontFamily),
            buttonTextStyle = theme.unscaledStyles.buttonTextStyle.copy(fontFamily = fontFamily),
        )
    )
}