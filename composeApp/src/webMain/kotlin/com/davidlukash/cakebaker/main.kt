package com.davidlukash.cakebaker

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.davidlukash.cakebaker.data.SavesRepository
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.viewmodel.MainViewModel

expect fun createSavesRepository(): SavesRepository

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val savesRepository = createSavesRepository()
    ComposeViewport {
        val localMainViewModel = remember { MainViewModel(savesRepository) }
        CompositionLocalProvider(
            LocalMainViewModel provides localMainViewModel
        ) {
            App()
        }
    }
}