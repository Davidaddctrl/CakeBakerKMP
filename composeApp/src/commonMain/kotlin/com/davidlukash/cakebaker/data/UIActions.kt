package com.davidlukash.cakebaker.data

import com.davidlukash.cakebaker.data.theme.json.JsonTheme
import com.davidlukash.cakebaker.ui.navigation.Screen
import kotlin.uuid.ExperimentalUuidApi

interface UIActions {
    fun addTextPopup(text: String)

    @OptIn(ExperimentalUuidApi::class)
    fun addTextButtonPopup(text: String, shouldHaveDefaultButton: Boolean = true, buttonText: String, onClick: () -> Unit)

    fun getCurrentScreen(): Screen?

    fun setDebugConsole(type: ConsoleType)

    fun triggerThemeImport(data: JsonTheme)
}