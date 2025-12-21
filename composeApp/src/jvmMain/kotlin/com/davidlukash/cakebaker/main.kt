package com.davidlukash.cakebaker
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.davidlukash.cakebaker.ui.DebugConsole
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.viewmodel.MainViewModel

const val usingDebugWindow = true

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
                    Row(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        DebugConsole()
                    }
                }
            }
        }
    }
}