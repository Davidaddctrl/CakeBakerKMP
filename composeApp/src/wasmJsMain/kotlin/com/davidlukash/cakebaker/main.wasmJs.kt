package com.davidlukash.cakebaker

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.davidlukash.cakebaker.repository.SavesRepository
import com.davidlukash.cakebaker.data.WASMSavesRepository
import com.davidlukash.cakebaker.data.log.Log
import com.davidlukash.cakebaker.logger.AppLogger
import kotlinx.browser.localStorage
import org.w3c.dom.get

actual fun createSavesRepository(): SavesRepository = WASMSavesRepository()

actual fun registerLogger() {
    logger.registerLogger(
        object : AppLogger() {
            override fun appendLog(log: Log) {
                localStorage.setItem("log", (localStorage["log"] ?: "") + "${log.toLogString()}\n")
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
actual fun viewport(content: @Composable (() -> Unit)) {
    ComposeViewport {
        content()
    }
}