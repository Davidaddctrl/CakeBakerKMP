package com.davidlukash.cakebaker.ui.navigation

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonTransformingSerializer
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
object FadeScreen : Screen("FadeScreen")

@Serializable
object KitchenScreen : Screen("KitchenScreen")

@Serializable
object IngredientScreen : Screen("IngredientScreen")

@Serializable
object UpgradeScreen : Screen("UpgradeScreen")

@Serializable
object SaveScreen : Screen("SaveScreen")

val allScreens = listOf(
    CakeScreen,
    MenuScreen,
    FadeScreen,
    KitchenScreen,
    IngredientScreen,
    UpgradeScreen,
    SaveScreen
)

val allScreensMap = allScreens.associateBy { Json.encodeToJsonElement(it as Screen).jsonObject["type"]?.jsonPrimitive?.content }