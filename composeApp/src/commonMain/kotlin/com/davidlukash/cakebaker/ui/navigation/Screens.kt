package com.davidlukash.cakebaker.ui.navigation

import com.davidlukash.cakebaker.ui.navigation.Screen.Cake
import com.davidlukash.cakebaker.ui.navigation.Screen.Fade
import com.davidlukash.cakebaker.ui.navigation.Screen.Ingredient
import com.davidlukash.cakebaker.ui.navigation.Screen.Kitchen
import com.davidlukash.cakebaker.ui.navigation.Screen.Menu
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
sealed class Screen {
    @Serializable
    object Cake : Screen()

    @Serializable
    object Menu : Screen()

    @Serializable
    object Fade : Screen()

    @Serializable
    object Kitchen : Screen()

    @Serializable
    object Ingredient : Screen()

    @Serializable
    object Upgrade : Screen()
}

val allScreens = listOf(
    Cake,
    Menu,
    Fade,
    Kitchen,
    Ingredient,
    Screen.Upgrade,
)

val allScreensMap = allScreens.associateBy { Json.encodeToJsonElement(it).jsonObject["type"]?.jsonPrimitive?.content }