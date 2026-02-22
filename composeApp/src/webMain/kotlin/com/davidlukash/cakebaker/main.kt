package com.davidlukash.cakebaker

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.repository.SavesRepository
import com.davidlukash.cakebaker.repository.ThemesRepository
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

expect fun createSavesRepository(): SavesRepository
expect fun createThemesRepository(): ThemesRepository
val savesRepository = createSavesRepository()
val themesRepository = createThemesRepository()
val mainViewModel = MainViewModel(savesRepository, themesRepository)
expect fun registerLogger()

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CoroutineScope(Dispatchers.Main).launch {
        withResultSuspend {
            Save.readOldSaves()
        }
    }
    registerLogger()
    ComposeViewport {
        CompositionLocalProvider(
            LocalMainViewModel provides mainViewModel
        ) {
            App()
        }
    }
}