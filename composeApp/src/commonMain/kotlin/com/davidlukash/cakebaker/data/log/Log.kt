package com.davidlukash.cakebaker.data.log

import com.davidlukash.cakebaker.currentLocalTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Log @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class) constructor(
    val message: String,
    val logType: LogType,
    val timestamp: LocalTime = currentLocalTime(),
    val uuid: Uuid = Uuid.random()
) {
    fun toLogString(): String = "[$logType] [${timestamp.format(LocalTime.Formats.ISO)}] $message"
}