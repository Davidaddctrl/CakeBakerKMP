package com.davidlukash.cakebaker.data.theme

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
import cakebaker.composeapp.generated.resources.green_arrow_up
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
import com.davidlukash.cakebaker.data.theme.json.BrushDescriptor
import com.davidlukash.cakebaker.data.theme.json.JsonTheme
import com.davidlukash.cakebaker.takeOrDefaultWithWarn
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.FontResource

@Composable
fun convertStyles(styles: TextStyles, externalFont: Font?, font: FontResource): TextStyles {
    val fontFamily = FontFamily(
        externalFont ?: Font(font)
    )
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
    return applyLocals(
        style.copy(
            fontFamily = fontFamily,
        )
    )
}

@Composable
fun applyLocals(style: TextStyle): TextStyle {
    val density = LocalDensity.current
    val shadow = if (LocalDoDropShadow.current) style.shadow else null
    val brush = LocalTextStyle.current.brush
    val weight = LocalTextStyle.current.fontWeight
    return style.copy(
        shadow = shadow?.copy(
            offset = density.run { shadow.offset.copy(x = shadow.offset.x.dp.toPx(), y = shadow.offset.y.dp.toPx()) }
        ),
        brush = brush,
        fontWeight = weight
    )
}

data class Theme(
    val idToImageMap: Map<String, ImageData>,
    val idToStringMap: Map<String, String>,
    val font: FontResource,
    val externalFont: Font?,
    val _scaledStyles: TextStyles,
    val _unscaledStyles: TextStyles,
    val progressBarTheme: ProgressBarTheme,
    val buttonTheme: ButtonTheme,
    val switchButtonTheme: SwitchButtonTheme,
    val backgroundTheme: ContainerTheme,
    val containerTheme: ContainerTheme,
    val secondaryContainerTheme: ContainerTheme,
    val textFieldTheme: TextFieldTheme,
    val successColor: Color,
    val dangerColor: Color,
    val tabSelectedColor: Color,
    val largeButtonValues: ButtonValues,
    val smallButtonValues: ButtonValues,
    val backgroundValues: ContainerValues,
    val largePrimaryContainerValues: ContainerValues,
    val largeSecondaryContainerValues: ContainerValues,
    val smallPrimaryContainerValues: ContainerValues,
    val smallSecondaryContainerValues: ContainerValues,
    val progressBarValues: ProgressBarValues
) {
    fun toJsonTheme() = JsonTheme(
        idToImageMap = idToImageMap.map { (key, value) ->
            key to (value.imagePath)
        }.toMap(),
        idToStringMap = idToStringMap,
        scaledStyles = _scaledStyles.toJsonTheme(),
        unscaledStyles = _unscaledStyles.toJsonTheme(),
        progressBarTheme = progressBarTheme.toJsonTheme(),
        buttonTheme = buttonTheme.toJsonTheme(),
        switchButtonTheme = switchButtonTheme.toJsonTheme(),
        backgroundTheme = backgroundTheme.toJsonTheme(),
        containerTheme = containerTheme.toJsonTheme(),
        secondaryContainerTheme = secondaryContainerTheme.toJsonTheme(),
        textFieldTheme = textFieldTheme.toJsonTheme(),
        successColor = successColor,
        dangerColor = dangerColor,
        tabSelectedColor = tabSelectedColor,
        largeButtonValues = largeButtonValues.toJsonValues(),
        smallButtonValues = smallButtonValues.toJsonValues(),
        backgroundValues = backgroundValues.toJsonValues(),
        largePrimaryContainerValues = largePrimaryContainerValues.toJsonValues(),
        largeSecondaryContainerValues = largeSecondaryContainerValues.toJsonValues(),
        smallPrimaryContainerValues = smallPrimaryContainerValues.toJsonValues(),
        smallSecondaryContainerValues = smallSecondaryContainerValues.toJsonValues(),
        progressBarValues = progressBarValues.toJsonValues(),
    )

    fun idToImage(name: String): ImageData {
        return idToImageMap[name].takeOrDefaultWithWarn("Image resource with id $name not found", ImageData())
    }

    fun idToString(name: String): String {
        return idToStringMap[name].takeOrDefaultWithWarn("String resource with id $name not found", name)
    }

    val styles: TextStyles
        @Composable
        get() {
            return convertStyles(
                if (LocalIsScaled.current) {
                    _scaledStyles
                } else _unscaledStyles, externalFont, font
            )
        }

    companion object {
        val Styles: TextStyles
            @Composable
            get() = LocalTheme.current.styles

        val ProgressBarTheme: ProgressBarTheme
            @Composable
            get() = LocalTheme.current.progressBarTheme

        val ButtonTheme: ButtonTheme
            @Composable
            get() = LocalTheme.current.buttonTheme

        val SwitchButtonTheme: SwitchButtonTheme
            @Composable
            get() = LocalTheme.current.switchButtonTheme

        val BackgroundTheme: ContainerTheme
            @Composable
            get() = LocalTheme.current.backgroundTheme

        val ContainerTheme: ContainerTheme
            @Composable
            get() = LocalTheme.current.containerTheme

        val SecondaryContainerTheme: ContainerTheme
            @Composable
            get() = LocalTheme.current.secondaryContainerTheme

        val TextFieldTheme: TextFieldTheme
            @Composable
            get() = LocalTheme.current.textFieldTheme

        val SuccessColor: Color
            @Composable
            get() = LocalTheme.current.successColor

        val DangerColor: Color
            @Composable
            get() = LocalTheme.current.dangerColor

        val TabSelectedColor: Color
            @Composable
            get() = LocalTheme.current.tabSelectedColor

        val LargeButtonValues: ButtonValues
            @Composable
            get() = LocalTheme.current.largeButtonValues

        val SmallButtonValues: ButtonValues
            @Composable
            get() = LocalTheme.current.smallButtonValues

        val LargePrimaryContainerValues: ContainerValues
            @Composable
            get() = LocalTheme.current.largePrimaryContainerValues

        val LargeSecondaryContainerValues: ContainerValues
            @Composable
            get() = LocalTheme.current.largeSecondaryContainerValues

        val SmallPrimaryContainerValues: ContainerValues
            @Composable
            get() = LocalTheme.current.smallPrimaryContainerValues

        val SmallSecondaryContainerValues: ContainerValues
            @Composable
            get() = LocalTheme.current.smallSecondaryContainerValues

        val BackgroundValues: ContainerValues
            @Composable
            get() = LocalTheme.current.backgroundValues

        val ProgressBarValues: ProgressBarValues
            @Composable
            get() = LocalTheme.current.progressBarValues

        @Composable
        fun getImage(name: String): ImageData {
            val theme = LocalTheme.current
            return remember(theme, name) { theme.idToImage(name) }
        }

        @Composable
        fun getString(name: String): String {
            val theme = LocalTheme.current
            return remember(name, theme) { theme.idToString(name) }
        }

        val default = Theme(
            idToImageMap = mapOf(
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

                "image.butter" to ImageData(resource = Res.drawable.butter),
                "image.egg" to ImageData(resource = Res.drawable.egg),
                "image.flour" to ImageData(resource = Res.drawable.flour),
                "image.sugar" to ImageData(resource = Res.drawable.sugar),
                "image.vanilla_extract" to ImageData(resource = Res.drawable.vanilla_extract),
                "image.baking_powder" to ImageData(resource = Res.drawable.baking_powder),
                "image.cocoa_powder" to ImageData(resource = Res.drawable.cocoa_powder),
                "image.honey_pot" to ImageData(resource = Res.drawable.honey_pot),
                "image.vanilla_cake" to ImageData(resource = Res.drawable.vanilla_cake),
                "image.chocolate_cake" to ImageData(resource = Res.drawable.choc_cake),
                "image.honey_cake" to ImageData(resource = Res.drawable.honey_cake),
                "image.money" to ImageData(resource = Res.drawable.money),
                "image.ingredient_shop" to ImageData(resource = Res.drawable.ingredient_shop),
                "image.upgrade_shop" to ImageData(resource = Res.drawable.upgrade_shop),
                "image.oven" to ImageData(resource = Res.drawable.oven),
                "image.face.happy" to ImageData(resource = Res.drawable.face_happy),
                "image.face.medium" to ImageData(resource = Res.drawable.face_medium),
                "image.face.neutral" to ImageData(resource = Res.drawable.face_neutral),
                "image.face.neutral_sad" to ImageData(resource = Res.drawable.face_neutral_sad),
                "image.face.sad" to ImageData(resource = Res.drawable.face_sad),
                "image.arrow.green_up" to ImageData(resource = Res.drawable.green_arrow_up),
                "image.missing" to ImageData()
            ),
            idToStringMap = mapOf(
                "item.butter.name" to "Butter",
                "item.egg.name" to "Egg",
                "item.flour.name" to "Flour",
                "item.sugar.name" to "Sugar",
                "item.vanilla_extract.name" to "Vanilla Extract",
                "item.baking_powder.name" to "Baking Powder",
                "item.cocoa_powder.name" to "Cocoa Powder",
                "item.honey_pot.name" to "Honey Pot",
                "item.vanilla_cake.name" to "Vanilla Cake",
                "item.chocolate_cake.name" to "Chocolate Cake",
                "item.honey_cake.name" to "Honey Cake",
                "item.money.name" to "Money",

                "action.buy" to "Buy",
                "action.delete" to "Delete",
                "action.dismiss" to "Dismiss",
                "action.overwrite" to "Overwrite",
                "action.delete" to "Delete",
                "action.import" to "Import",
                "action.export" to "Export",
                "action.yes" to "Yes",
                "action.no" to "No",
                "action.create" to "Create",
                "action.complete" to "Complete",
                "action.load" to "Load",
                "action.cancel" to "Cancel",
                "action.on" to "On",
                "action.off" to "Off",
                "action.play" to "Play",
                "action.saves" to "Saves",
                "action.licenses" to "Licenses",
                "action.settings" to "Settings",
                "action.back" to "Back",
                "action.themes" to "Themes",
                "action.apply" to "Apply",
                "action.import_default_themes" to "Import Default Themes",
                "action.import_from_url" to "Import from URL",

                "content_description.menu" to "Menu",
                "content_description.previous_tier" to "Previous Tier",
                "content_description.next_tier" to "Next Tier",
                "content_description.enough" to "Enough",
                "content_description.not_enough" to "Not Enough",
                "content_description.select_theme" to "Select Theme",
                "content_description.deselect_theme" to "Deselect Theme",
                "content_description.export_theme" to "Export Theme",
                "content_description.delete_theme" to "Delete Theme",
                "content_description.move_up" to "Move Up",
                "content_description.move_down" to "Move Down",

                "label.amount" to "Amount",
                "label.customer_satisfaction" to "Customer\nSatisfaction",
                "label.cake_sale_price" to "Cake Sale Price",
                "label.auto_oven" to "Auto Oven",
                "label.auto_order_complete" to "Auto Order Complete",
                "label.seconds_remaining" to "{0} seconds remaining",
                "label.remaining" to "{0} remaining",
                "label.order_for" to "Order For",
                "label.buying_for" to "Buying For",
                "label.remaining_time" to "Remaining Time",
                "label.order" to "Order",
                "label.invalid_cake_tier" to "Invalid Cake Tier",
                "label.seconds" to "seconds",
                "label.version" to "Version: {0}",
                "label.level" to "Level",
                "label.max_level" to "Max Level Reached",

                "title.cake_baker" to "Cake Baker",
                "title.kitchen" to "Kitchen",
                "title.ingredient_shop" to "Ingredient Shop",
                "title.information" to "Information",
                "title.orders" to "Orders",
                "title.license_for" to "License For {0}",
                "title.licenses" to "Licenses",
                "title.saves" to "Saves",
                "title.settings" to "Settings",
                "title.upgrade_shop" to "Upgrade Shop",
                "title.themes" to "Themes",
                "title.available" to "Available",
                "title.selected" to "Selected",

                "settings.debug_console_open.title" to "Debug Console Open",
                "settings.debug_console_open.open" to "Open",
                "settings.debug_console_open.closed" to "Closed",

                "orders_panel.until_next_order" to "{0} until next order",
                "orders_panel.help_text" to "Bake a cake to get orders",

                "dialog.load_save.title" to "Load Save",
                "dialog.load_save.load_text" to "Loading save \"{0}\" will overwrite your current progress.",
                "dialog.load_save.migration_text" to "There are recommended migrations you may choose to apply that will not overwrite the save.",
                "dialog.load_save.migration_title" to "Migrate?",

                "dialog.delete_save.title" to "Delete Save",
                "dialog.delete_save.delete_text" to "Are you sure you want to delete save \"{0}\" permanently?",

                "dialog.overwrite_save.title" to "Overwrite Save",
                "dialog.overwrite_save.overwrite_text" to "Are you sure you want to overwrite save \"{0}\"? You cannot revert this.",

                "dialog.create_save.create_title" to "Create Save",
                "dialog.create_save.import_title" to "Import Save",
                "dialog.create_save.save_name_field.title" to "Save Name",
                "dialog.create_save.error.already_exists" to "A save with this name already exists",
                "dialog.create_save.error.invalid_name" to "Save name must not be blank and must only contain lowercase alphanumeric characters",

                "dialog.delete_theme.title" to "Delete Theme",
                "dialog.delete_theme.delete_text" to "Are you sure you want to delete theme \"{0}\" permanently?",

                "dialog.import_theme.title" to "Import Theme",
                "dialog.import_theme.theme_name_field.title" to "Theme Name",
                "dialog.import_theme.error.already_exists" to "A theme with this name already exists",
                "dialog.import_theme.error.invalid_name" to "Theme name must not be blank and must only contain lowercase alphanumeric characters",


                "upgrade.expensive_vanilla_cakes.name" to "Expensive Vanilla Cakes",
                "upgrade.page_name.cake" to "Cake",
                "upgrade.expensive_chocolate_cakes.name" to "Expensive Chocolate Cakes",
                "upgrade.page_name.cake" to "Cake",
                "upgrade.expensive_honey_cakes.name" to "Expensive Honey Cakes",
                "upgrade.page_name.cake" to "Cake",
                "upgrade.expensive_cat_cakes.name" to "Expensive Cat Cakes",
                "upgrade.page_name.cake" to "Cake",
                "upgrade.faster_oven.name" to "Faster Oven",
                "upgrade.page_name.oven" to "Oven",
                "upgrade.auto_oven.name" to "Auto Oven",
                "upgrade.page_name.oven" to "Oven",
                "upgrade.auto_order_complete.name" to "Auto Order Complete",
                "upgrade.page_name.orders" to "Orders",
                "upgrade.cheaper_butter.name" to "Cheaper Butter",
                "upgrade.page_name.butter" to "Butter",
                "upgrade.dense_butter.name" to "Dense Butter",
                "upgrade.page_name.butter" to "Butter",
                "upgrade.cheaper_egg.name" to "Cheaper Egg",
                "upgrade.page_name.egg" to "Egg",
                "upgrade.dense_egg.name" to "Dense Egg",
                "upgrade.page_name.egg" to "Egg",
                "upgrade.cheaper_flour.name" to "Cheaper Flour",
                "upgrade.page_name.flour" to "Flour",
                "upgrade.dense_flour.name" to "Dense Flour",
                "upgrade.page_name.flour" to "Flour",
                "upgrade.cheaper_sugar.name" to "Cheaper Sugar",
                "upgrade.page_name.sugar" to "Sugar",
                "upgrade.dense_sugar.name" to "Dense Sugar",
                "upgrade.page_name.sugar" to "Sugar",
                "upgrade.cheaper_vanilla_extract.name" to "Cheaper Vanilla Extract",
                "upgrade.page_name.vanilla_extract" to "Vanilla Extract",
                "upgrade.dense_vanilla_extract.name" to "Dense Vanilla Extract",
                "upgrade.page_name.vanilla_extract" to "Vanilla Extract",
                "upgrade.cheaper_baking_powder.name" to "Cheaper Baking Powder",
                "upgrade.page_name.baking_powder" to "Baking Powder",
                "upgrade.dense_baking_powder.name" to "Dense Baking Powder",
                "upgrade.page_name.baking_powder" to "Baking Powder",

                "dialog.import_url_theme.title" to "Import Theme from URL",
                "dialog.import_url_theme.theme_name_field.title" to "URL",
                "dialog.import_url_theme.theme_name_field.placeholder" to "https://...",

                "dialog.wait_for_import_theme.title" to "Importing Themes",
                "dialog.wait_for_import_theme.text" to "Please wait. Importing external themes."
            ),
            font = Res.font.vcr_osd_mono,
            externalFont = null,
            _scaledStyles = TextStyles(
                titleStyle = TextStyle(fontSize = 72.sp, shadow = Shadow(color = Color.Black, offset = Offset(0f, 4f))),
                buttonTextStyle = TextStyle(
                    fontSize = 60.sp,
                    shadow = Shadow(color = Color.Black, offset = Offset(0f, 4f))
                ),
                largeBodyStyle = TextStyle(
                    fontSize = 48.sp,
                    shadow = Shadow(color = Color.Black, offset = Offset(0f, 3f))
                ),
                mediumBodyStyle = TextStyle(
                    fontSize = 36.sp,
                    shadow = Shadow(color = Color.Black, offset = Offset(0f, 3f))
                ),
                smallBodyStyle = TextStyle(
                    fontSize = 32.sp,
                    shadow = Shadow(color = Color.Black, offset = Offset(0f, 3f))
                ),
                verySmallBodyStyle = TextStyle(
                    fontSize = 22.sp,
                    shadow = Shadow(color = Color.Black, offset = Offset(0f, 2f))
                ),
            ),
            _unscaledStyles = TextStyles(
                titleStyle = TextStyle(fontSize = 36.sp, shadow = Shadow(color = Color.Black, offset = Offset(0f, 2f))),
                buttonTextStyle = TextStyle(
                    fontSize = 22.sp,
                    shadow = Shadow(color = Color.Black, offset = Offset(0f, 2f))
                ),
                largeBodyStyle = TextStyle(
                    fontSize = 22.sp,
                    shadow = Shadow(color = Color.Black, offset = Offset(0f, 2f))
                ),
                mediumBodyStyle = TextStyle(
                    fontSize = 18.sp,
                    shadow = Shadow(color = Color.Black, offset = Offset(0f, 2f))
                ),
                smallBodyStyle = TextStyle(
                    fontSize = 12.sp,
                    shadow = Shadow(color = Color.Black, offset = Offset(0f, 2f))
                ),
                verySmallBodyStyle = TextStyle(
                    fontSize = 12.sp,
                    shadow = Shadow(color = Color.Black, offset = Offset(0f, 2f))
                )
            ),
            progressBarTheme = ProgressBarTheme(
                borderColorBrushDescriptor = BrushDescriptor.SolidColor(Color.Black),
                backgroundColorBrushDescriptor = BrushDescriptor.SolidColor(Color(127, 127, 127)),
                filledColorBrushDescriptor = BrushDescriptor.SolidColor(Color(255, 127, 0)),
                contentColorBrushDescriptor = BrushDescriptor.SolidColor(Color.White),
                contentBold = false,
                shouldDropShadow = true
            ),
            buttonTheme = ButtonTheme(
                containerColorBrushDescriptor = BrushDescriptor.SolidColor(Color(8, 160, 69)),
                disabledContainerColorBrushDescriptor = BrushDescriptor.SolidColor(Color(37, 41, 46)),
                contentColorBrushDescriptor = BrushDescriptor.SolidColor(Color(255, 255, 255)),
                disabledContentColorBrushDescriptor = BrushDescriptor.SolidColor(Color(128, 128, 128)),
                borderColorBrushDescriptor = BrushDescriptor.SolidColor(Color(0, 0, 0)),
                disabledBorderColorBrushDescriptor = BrushDescriptor.SolidColor(Color(53, 57, 62)),
                contentBold = false,
                shouldDropShadow = true
            ),
            switchButtonTheme = SwitchButtonTheme(
                containerColor = Color(0, 0, 0),
                disabledContainerColor = Color(0, 0, 0),
                borderColor = Color(0, 0, 0),
                disabledBorderColor = Color(0, 0, 0),
                offSelectedContainerColor = Color(255, 0, 0),
                offUnselectedContainerColor = Color(255, 0, 0).copy(alpha = 0.3f),
                offSelectedTextColor = Color(255, 255, 255),
                offUnselectedTextColor = Color(255, 255, 255).copy(alpha = 0.3f),
                onSelectedContainerColor = Color(58, 158, 0),
                onUnselectedContainerColor = Color(58, 158, 0).copy(alpha = 0.3f),
                onSelectedTextColor = Color(255, 255, 255),
                onUnselectedTextColor = Color(255, 255, 255).copy(alpha = 0.3f),
                shouldDropShadow = false
            ),
            backgroundTheme = ContainerTheme(
                borderColorBrushDescriptor = BrushDescriptor.SolidColor(Color.Transparent),
                containerColorBrushDescriptor = BrushDescriptor.VerticalGradient(
                    listOf(
                        Color(0, 100, 217, 255),
                        Color(0, 120, 255),
                        Color(0, 120, 255),
                        Color(0, 120, 255),
                        Color(0, 120, 255),
                        Color(0, 120, 255),
                        Color(0, 120, 255),
                    )
                ),
                contentColorBrushDescriptor = BrushDescriptor.SolidColor(Color(255, 255, 255)),
                contentBold = false,
                shouldDropShadow = true
            ),
            containerTheme = ContainerTheme(
                borderColorBrushDescriptor = BrushDescriptor.SolidColor(Color(0, 0, 0)),
                containerColorBrushDescriptor = BrushDescriptor.SolidColor(Color(246, 255, 153)),
                contentColorBrushDescriptor = BrushDescriptor.SolidColor(Color(0, 0, 0)),
                contentBold = false,
                shouldDropShadow = false
            ),
            secondaryContainerTheme = ContainerTheme(
                borderColorBrushDescriptor = BrushDescriptor.SolidColor(Color(0, 0, 0)),
                containerColorBrushDescriptor = BrushDescriptor.SolidColor(Color(8, 160, 69)),
                contentColorBrushDescriptor = BrushDescriptor.SolidColor(Color(255, 255, 255)),
                contentBold = false,
                shouldDropShadow = true
            ),
            textFieldTheme = TextFieldTheme(
                cursorBrushDescriptor = BrushDescriptor.SolidColor(Color.White),
                contentColor = Color.White,
                placeholderColor = Color(128, 128, 128),
                containerColor = Color(53, 57, 62),
                borderColor = Color(37, 41, 46),
                shouldDropShadow = false
            ),
            successColor = Color(58, 158, 0),
            dangerColor = Color(255, 0, 0),
            tabSelectedColor = Color(8, 160, 69),
            largeButtonValues = ButtonValues(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                focusedElevation = 0.dp,
                hoveredElevation = 0.dp,
                disabledElevation = 0.dp,
                borderRadius = 16.dp,
                borderWidth = 8.dp,
                contentPadding = 16.dp
            ),
            smallButtonValues = ButtonValues(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                focusedElevation = 0.dp,
                hoveredElevation = 0.dp,
                disabledElevation = 0.dp,
                borderRadius = 8.dp,
                borderWidth = 4.dp,
                contentPadding = 8.dp
            ),
            backgroundValues = ContainerValues(
                elevation = 0.dp,
                borderRadius = 0.dp,
                borderWidth = 0.dp,
                contentPadding = 16.dp
            ),
            largePrimaryContainerValues = ContainerValues(
                elevation = 0.dp,
                borderRadius = 16.dp,
                borderWidth = 8.dp,
                contentPadding = 16.dp
            ),
            largeSecondaryContainerValues = ContainerValues(
                elevation = 0.dp,
                borderRadius = 16.dp,
                borderWidth = 8.dp,
                contentPadding = 16.dp
            ),
            smallPrimaryContainerValues = ContainerValues(
                elevation = 0.dp,
                borderRadius = 8.dp,
                borderWidth = 6.dp,
                contentPadding = 16.dp
            ),
            smallSecondaryContainerValues = ContainerValues(
                elevation = 0.dp,
                borderRadius = 8.dp,
                borderWidth = 4.dp,
                contentPadding = 8.dp
            ),
            progressBarValues = ProgressBarValues(
                elevation = 0.dp,
                borderRadius = 48.dp,
                borderWidth = 8.dp,
                minHeight = 48.dp
            )
        )

//        val darkDefault = Theme(
//            idToImageMap = mapOf(
//                "Butter" to ImageData(resource = Res.drawable.butter),
//                "Egg" to ImageData(resource = Res.drawable.egg),
//                "Flour" to ImageData(resource = Res.drawable.flour),
//                "Sugar" to ImageData(resource = Res.drawable.sugar),
//                "Vanilla Extract" to ImageData(resource = Res.drawable.vanilla_extract),
//                "Baking Powder" to ImageData(resource = Res.drawable.baking_powder),
//                "Cocoa Powder" to ImageData(resource = Res.drawable.cocoa_powder),
//                "Honey Pot" to ImageData(resource = Res.drawable.honey_pot),
//                "Vanilla Cake" to ImageData(resource = Res.drawable.vanilla_cake),
//                "Chocolate Cake" to ImageData(resource = Res.drawable.choc_cake),
//                "Honey Cake" to ImageData(resource = Res.drawable.honey_cake),
//                "Money" to ImageData(resource = Res.drawable.money),
//                "Ingredient Shop" to ImageData(resource = Res.drawable.ingredient_shop),
//                "Upgrade Shop" to ImageData(resource = Res.drawable.upgrade_shop),
//                "Oven" to ImageData(resource = Res.drawable.oven),
//                "Happy Face" to ImageData(resource = Res.drawable.face_happy),
//                "Medium Face" to ImageData(resource = Res.drawable.face_medium),
//                "Neutral Face" to ImageData(resource = Res.drawable.face_neutral),
//                "Neutral Sad Face" to ImageData(resource = Res.drawable.face_neutral_sad),
//                "Sad Face" to ImageData(resource = Res.drawable.face_sad),
//            ),
//            font = Res.font.vcr_osd_mono,
//            _scaledStyles = TextStyles(
//                titleStyle = TextStyle(fontSize = 72.sp),
//                buttonTextStyle = TextStyle(fontSize = 60.sp),
//                largeBodyStyle = TextStyle(fontSize = 48.sp),
//                mediumBodyStyle = TextStyle(fontSize = 36.sp),
//                smallBodyStyle = TextStyle(fontSize = 32.sp),
//                verySmallBodyStyle = TextStyle(fontSize = 22.sp),
//            ),
//            _unscaledStyles = TextStyles(
//                titleStyle = TextStyle(fontSize = 36.sp),
//                buttonTextStyle = TextStyle(fontSize = 22.sp),
//                largeBodyStyle = TextStyle(fontSize = 22.sp),
//                mediumBodyStyle = TextStyle(fontSize = 12.sp),
//                smallBodyStyle = TextStyle(fontSize = 12.sp),
//                verySmallBodyStyle = TextStyle(fontSize = 12.sp)
//            ),
//            progressBarTheme = ProgressBarTheme(
//                border = Color.White,
//                backgroundColor = Color(0, 0, 0),
//                filledColor = Color(255, 127, 0)
//            ),
//            buttonTheme = ButtonTheme(
//                containerColor = Color(0, 0, 0),
//                disabledContainerColor = Color(0, 0, 0),
//                contentColor = Color(255, 255, 255),
//                disabledContentColor = Color(128, 128, 128),
//                borderColor = Color(255, 255, 255),
//                disabledBorderColor = Color(160, 160, 160),
//                shouldDropShadow = true
//            ),
//            switchButtonTheme = SwitchButtonTheme(
//                containerColor = Color(0, 0, 0),
//                disabledContainerColor = Color(0, 0, 0),
//                borderColor = Color(255, 255, 255),
//                disabledBorderColor = Color(255, 255, 255),
//                offSelectedContainerColor = Color(0, 0, 0),
//                offUnselectedContainerColor = Color(0, 0, 0),
//                offSelectedTextColor = Color(255, 255, 255),
//                offUnselectedTextColor = Color(128, 128, 128),
//                onSelectedContainerColor = Color(0, 0, 0),
//                onUnselectedContainerColor = Color(0, 0, 0),
//                onSelectedTextColor = Color(255, 255, 255),
//                onUnselectedTextColor = Color(128, 128, 128)
//            ),
//            backgroundTheme = ContainerTheme(
//                borderColor = Color.Transparent,
//                containerColor = Color(0, 0, 0),
//                contentColor = Color(255, 255, 255),
//                shouldDropShadow = false
//            ),
//            containerTheme = ContainerTheme(
//                borderColor = Color(255, 255, 255),
//                containerColor = Color(0, 0, 0),
//                contentColor = Color(255, 255, 255),
//                shouldDropShadow = false
//            ),
//            secondaryContainerTheme = ContainerTheme(
//                borderColor = Color(255, 255, 255),
//                containerColor = Color(0, 0, 0),
//                contentColor = Color(255, 255, 255),
//                shouldDropShadow = false
//            ),
//            successColor = Color(255, 255, 255),
//            dangerColor = Color(255, 255, 255),
//            tabSelectedColor = Color(8, 160, 69)
//        )
    }
}

val LocalDoDropShadow = compositionLocalOf { true }
val LocalIsScaled = compositionLocalOf { true }
val LocalTheme = compositionLocalOf { Theme.default }