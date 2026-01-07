package com.davidlukash.cakebaker.ui.navigation

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
sealed class Screen(val name: String)

@Serializable
object CakeScreen : Screen("CakeScreen")

@Serializable
object MenuScreen : Screen("MenuScreen")

@Serializable
object KitchenScreen : Screen("KitchenScreen")

@Serializable
object IngredientScreen : Screen("IngredientScreen")

@Serializable
object UpgradeScreen : Screen("UpgradeScreen")

@Serializable
object SaveScreen : Screen("SaveScreen")
@Serializable
object OtherScreen : Screen("OtherScreen")
@Serializable
object SettingsScreen : Screen("SettingsScreen")

val allScreens = listOf(
    CakeScreen,
    MenuScreen,
    KitchenScreen,
    IngredientScreen,
    UpgradeScreen,
    SaveScreen,
    OtherScreen,
    SettingsScreen
)

val allScreensMap = allScreens.associateBy { Json.encodeToJsonElement(it as Screen).jsonObject["type"]?.jsonPrimitive?.content }