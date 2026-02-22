package com.davidlukash.cakebaker

import com.davidlukash.cakebaker.repository.SavesRepository
import com.davidlukash.cakebaker.data.WASMSavesRepository
import com.davidlukash.cakebaker.data.WASMThemesRepository
import com.davidlukash.cakebaker.data.log.Log
import com.davidlukash.cakebaker.logger.AppLogger
import com.davidlukash.cakebaker.repository.ThemesRepository
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

actual fun createThemesRepository(): ThemesRepository = WASMThemesRepository()