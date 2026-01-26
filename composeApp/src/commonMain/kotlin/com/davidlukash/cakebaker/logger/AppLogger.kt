package com.davidlukash.cakebaker.logger

import com.davidlukash.cakebaker.data.log.Log
import com.davidlukash.cakebaker.data.log.LogType
import com.davidlukash.jsonmath.engine.basic.toTraceString
import com.davidlukash.jsonmath.engine.normal.LanguageException
import kotlin.uuid.ExperimentalUuidApi

abstract class AppLogger {
    abstract fun appendLog(log: Log)

    @OptIn(ExperimentalUuidApi::class)
    fun logDebug(message: String) {
        appendLog(Log(message, LogType.DEBUG))
    }

    @OptIn(ExperimentalUuidApi::class)
    fun logInfo(message: String) {
        appendLog(Log(message, LogType.INFO))
    }

    @OptIn(ExperimentalUuidApi::class)
    fun logWarn(message: String) {
        appendLog(Log(message, LogType.WARN))
    }

    @OptIn(ExperimentalUuidApi::class)
    fun logError(error: Exception) {
        if (error is LanguageException) {
            logErrorMessage(error.toString() + error.origins?.toTraceString())
        } else {
            logErrorMessage(error.stackTraceToString())
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun logErrorMessage(message: String) {
        appendLog(Log(message, LogType.ERROR))
    }
}