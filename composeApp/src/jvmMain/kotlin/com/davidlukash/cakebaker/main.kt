package com.davidlukash.cakebaker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.davidlukash.cakebaker.data.ConsoleType
import com.davidlukash.cakebaker.data.JVMSavesRepository
import com.davidlukash.cakebaker.ui.DebugPanel
import com.davidlukash.cakebaker.ui.ErrorPanel
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.viewmodel.MainViewModel
import java.io.File
import javax.swing.UIManager

fun main() {
    try {
        val baseDirectory = File(".").absoluteFile.resolve("CakeBaker").also { it.mkdirs() }
        val savesRepository = JVMSavesRepository(baseDirectory)
        val viewModel = MainViewModel(savesRepository)
        withErrorHandling(viewModel.uiViewModel) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        }
        try {
            application {
                val localMainViewModel = remember { viewModel }
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
        } catch (e: Throwable) {
            application {
                Window(
                    onCloseRequest = ::exitApplication,
                    title = "Cake Baker - An error has occurred that the game cannot recover from"
                ) {
                    val errors = mutableStateListOf(e)
                    ErrorPanel(
                        errors = errors,
                        viewModel = viewModel,
                        quitApp = ::exitApplication
                    ) { errors.add(it) }
                }
            }
        }
    } catch (e: Throwable) {
        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = "Cake Baker - An error has occurred that the game cannot recover from"
            ) {
                val errors = mutableStateListOf(e)
                ErrorPanel(
                    errors = errors,
                    quitApp = ::exitApplication
                ) { errors.add(it) }
            }
        }
    }
}