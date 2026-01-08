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
    val backgroundColor: Color,
    val accentColor: Color,
    val nameToImageMap: Map<String, ImageData>,
    val font: FontFamily,
    val titleStyle: TextStyle,
    val smallTitleStyle: TextStyle,
    val subtitleStyle: TextStyle,
    val labelStyle: TextStyle,
    val smallLabelStyle: TextStyle,
    val verySmallLabelStyle: TextStyle,
    val buttonTextStyle: TextStyle,
    val extremelySmallLabelStyle: TextStyle,
    val containerBorderColor: Color,
    val progressBarTheme: ProgressBarTheme,
    val buttonTheme: ButtonTheme,
    val switchButtonTheme: SwitchButtonTheme,
    val goodColor: Color,
    val badColor: Color,
) {

    fun nameToImage(name: String): ImageData {
        return nameToImageMap[name] ?: ImageData()
    }

    companion object {
        //Do not use this, use getDefaultTheme instead
        val default = Theme(
            backgroundColor = Color(0, 120, 255),
            accentColor = Color(246, 255, 153),
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
            titleStyle = TextStyle(fontSize = 72.sp),
            smallTitleStyle = TextStyle(fontSize = 48.sp),
            subtitleStyle = TextStyle(fontSize = 36.sp),
            labelStyle = TextStyle(fontSize = 32.sp),
            smallLabelStyle = TextStyle(fontSize = 22.sp),
            verySmallLabelStyle = TextStyle(fontSize = 16.sp),
            buttonTextStyle = TextStyle(fontSize = 60.sp),
            extremelySmallLabelStyle = TextStyle(fontSize = 12.sp),
            buttonTheme = ButtonTheme(
                containerColor = Color(8, 160, 69),
                disabledContainerColor = Color(37, 41, 46),
                contentColor = Color(255, 255, 255),
                disabledContentColor = Color(128, 128, 128),
                borderColor = Color(0, 0, 0),
                disabledBorderColor = Color(53, 57, 62),
            ),
            progressBarTheme = ProgressBarTheme(
                border = Color.Black,
                backgroundColor = Color(127, 127, 127),
                filledColor = Color(255, 127, 0)
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
            containerBorderColor = Color.Black,
            goodColor = Color(58, 158, 0),
            badColor = Color(255, 0, 0)
        )
    }
}

@Composable
fun getDefaultTheme(): Theme {
    val theme = Theme.default
    val fontFamily = FontFamily(Font(Res.font.vcr_osd_mono))
    return theme.copy(
        font = fontFamily,
        titleStyle = theme.titleStyle.copy(fontFamily = fontFamily),
        smallTitleStyle = theme.smallTitleStyle.copy(fontFamily = fontFamily),
        subtitleStyle = theme.subtitleStyle.copy(fontFamily = fontFamily),
        labelStyle = theme.labelStyle.copy(fontFamily = fontFamily),
        smallLabelStyle = theme.smallLabelStyle.copy(fontFamily = fontFamily),
        verySmallLabelStyle = theme.verySmallLabelStyle.copy(fontFamily = fontFamily),
        buttonTextStyle = theme.buttonTextStyle.copy(fontFamily = fontFamily),
        extremelySmallLabelStyle = theme.extremelySmallLabelStyle.copy(fontFamily = fontFamily),
    )
}