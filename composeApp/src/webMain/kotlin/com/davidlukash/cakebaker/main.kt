package com.davidlukash.cakebaker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.davidlukash.cakebaker.repository.SavesRepository
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.viewmodel.MainViewModel

expect fun createSavesRepository(): SavesRepository
val savesRepository = createSavesRepository()
val mainViewModel = MainViewModel(savesRepository)
expect fun registerLogger()

expect fun viewport(content: @Composable () -> Unit)

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    registerLogger()
    viewport {
        CompositionLocalProvider(
            LocalMainViewModel provides mainViewModel
        ) {
            App()
        }
    }
}