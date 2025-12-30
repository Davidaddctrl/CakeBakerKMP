package com.davidlukash.cakebaker
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.davidlukash.cakebaker.ui.DebugPanel
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.viewmodel.MainViewModel

const val usingDebugWindow = false

fun main() {
    application {
        val localMainViewModel = remember { MainViewModel() }
        CompositionLocalProvider(
            LocalMainViewModel provides localMainViewModel
        ) {
            Window(
                onCloseRequest = ::exitApplication,
                title = "Cake Baker",
            ) {
                App()
            }
            if (usingDebugWindow) {
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