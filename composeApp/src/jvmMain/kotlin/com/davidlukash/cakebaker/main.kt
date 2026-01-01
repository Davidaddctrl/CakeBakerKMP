package com.davidlukash.cakebaker
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.davidlukash.cakebaker.data.ConsoleType
import com.davidlukash.cakebaker.ui.DebugPanel
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.viewmodel.MainViewModel

fun main() {
    application {
        val localMainViewModel = remember { MainViewModel() }
        CompositionLocalProvider(
            LocalMainViewModel provides localMainViewModel
        ) {
            val uiViewModel = localMainViewModel.uiViewModel
            val debugConsole by uiViewModel.debugConsole.collectAsState()
            Window(
                onCloseRequest = ::exitApplication,
                title = "Cake Baker",
            ) {
                App()
            }
            if (debugConsole == ConsoleType.WINDOW) {
                Window(
                    onCloseRequest = {},
                    title = "Cake Baker - Debug Console",
                ) {
                    DebugPanel(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}