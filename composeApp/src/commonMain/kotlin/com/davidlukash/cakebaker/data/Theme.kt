package com.davidlukash.cakebaker.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cakebaker.composeapp.generated.resources.Res
import cakebaker.composeapp.generated.resources.Roboto
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
import cakebaker.composeapp.generated.resources.missing
import cakebaker.composeapp.generated.resources.money
import cakebaker.composeapp.generated.resources.oven
import cakebaker.composeapp.generated.resources.sugar
import cakebaker.composeapp.generated.resources.upgrade_shop
import cakebaker.composeapp.generated.resources.vanilla_cake
import cakebaker.composeapp.generated.resources.vanilla_extract
import cakebaker.composeapp.generated.resources.vcr_osd_mono
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.FontResource

data class Theme(
    val backgroundColor: Color,
    val accentColor: Color,
//    val butterImage: ImageData,
//    val eggImage: ImageData,
//    val flourImage: ImageData,
//    val sugarImage: ImageData,
//    val vanillaExtractImage: ImageData,
//    val bakingPowderImage: ImageData,
//    val cocoaPowderImage: ImageData,
//    val honeyPotImage: ImageData,
//    val vanillaCakeImage: ImageData,
//    val chocolateCakeImage: ImageData,
//    val honeyCakeImage: ImageData,
//    val moneyImage: ImageData,
//    val ingredientShopImage: ImageData,
//    val upgradeShopImage: ImageData,
//    val ovenImage: ImageData,
//    val faceHappy: ImageData,
//    val faceMedium: ImageData,
//    val faceNeutral: ImageData,
//    val faceNeutralSad: ImageData,
//    val faceSad: ImageData,
    val nameToImageMap: Map<String, ImageData>,
    val font: FontResource,
    val titleStyle: TextStyle,
    val smallTitleStyle: TextStyle,
    val subtitleStyle: TextStyle,
    val labelStyle: TextStyle,
    val smallLabelStyle: TextStyle,
    val verySmallLabelStyle: TextStyle,
    val buttonTextStyle: TextStyle,
    val buttonTheme: ButtonTheme,
    val progressBarTheme: ProgressBarTheme,
    val containerBorderColor: Color,
    val green: Color,
    val red: Color,
) {

    fun nameToImage(name: String): ImageData {
        return nameToImageMap[name] ?: ImageData()
    }

    companion object {
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
            font = Res.font.vcr_osd_mono,
            titleStyle = TextStyle(fontSize = 72.sp),
            smallTitleStyle = TextStyle(fontSize = 48.sp),
            subtitleStyle = TextStyle(fontSize = 36.sp),
            labelStyle = TextStyle(fontSize = 32.sp),
            smallLabelStyle = TextStyle(fontSize = 22.sp),
            verySmallLabelStyle = TextStyle(fontSize = 16.sp),
            buttonTextStyle = TextStyle(fontSize = 56.sp),
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
            containerBorderColor = Color.Black,
            green = Color(58, 158, 0),
            red = Color(255, 0, 0),
        )

//        val default = Theme(
//            backgroundColor = Color(0, 0, 0),
//            accentColor = Color(255, 255, 255),
//            butterImage = ImageData(resource = Res.drawable.missing),
//            eggImage = ImageData(resource = Res.drawable.missing),
//            flourImage = ImageData(resource = Res.drawable.missing),
//            sugarImage = ImageData(resource = Res.drawable.missing),
//            vanillaExtractImage = ImageData(resource = Res.drawable.missing),
//            bakingPowderImage = ImageData(resource = Res.drawable.missing),
//            cocoaPowderImage = ImageData(resource = Res.drawable.missing),
//            honeyPotImage = ImageData(resource = Res.drawable.missing),
//            vanillaCakeImage = ImageData(resource = Res.drawable.missing),
//            chocolateCakeImage = ImageData(resource = Res.drawable.missing),
//            honeyCakeImage = ImageData(resource = Res.drawable.missing),
//            moneyImage = ImageData(resource = Res.drawable.missing),
//            ingredientShopImage = ImageData(resource = Res.drawable.missing),
//            upgradeShopImage = ImageData(resource = Res.drawable.missing),
//            ovenImage = ImageData(resource = Res.drawable.missing),
//            font = Res.font.Roboto,
//            titleStyle = TextStyle(fontSize = 96.sp),
//            smallTitleStyle = TextStyle(fontSize = 48.sp),
//            subtitleStyle = TextStyle(fontSize = 36.sp),
//            labelStyle = TextStyle(fontSize = 32.sp),
//            smallLabelStyle = TextStyle(fontSize = 22.sp),
//            buttonTextStyle = TextStyle(fontSize = 56.sp),
//            buttonTheme = ButtonTheme(
//                containerColor = Color(0, 0, 0),
//                disabledContainerColor = Color(37, 41, 46),
//                contentColor = Color(255, 255, 255),
//                disabledContentColor = Color(128, 128, 128),
//                borderColor = Color(255, 255, 255),
//                disabledBorderColor = Color(53, 57, 62),
//            ),
//            progressBarTheme = ProgressBarTheme(
//                border = Color.Black,
//                backgroundColor = Color(127, 127, 127),
//                filledColor = Color(255, 255, 255, 255)
//            ),
//            containerBorderColor = Color.Black,
//            green = Color(255, 0, 0, 255),
//            red = Color(255, 0, 0),
//        )
    }
}
