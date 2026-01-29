package com.davidlukash.cakebaker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.davidlukash.cakebaker.data.JSSavesRepository
import com.davidlukash.cakebaker.data.log.Log
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.logger.AppLogger
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.modifiers
import com.davidlukash.cakebaker.repository.SavesRepository
import kotlinx.browser.document
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.get

actual fun createSavesRepository(): SavesRepository = JSSavesRepository()

actual fun registerLogger() {
    logger.registerLogger(
        object : AppLogger() {
            override fun appendLog(log: Log) {
                localStorage.setItem("log", (localStorage["log"] ?: "") + "${log.toLogString()}\n")
            }
        }
    )
}

actual fun viewport(content: @Composable (() -> Unit)) {
    var hasRan = false
    document.onreadystatechange = {
        if (!hasRan) {
            hasRan = true
            renderComposable(rootElementId = "root") {
                CompositionLocalProvider(
                    LocalDensity provides Density(1f, 1f)
                ) {
                    content()
                }
            }
        }
    }
}