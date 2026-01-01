package com.davidlukash.cakebaker.data

import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.currentLocalTime
import kotlinx.datetime.LocalTime
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Log @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class) constructor(
    val message: String,
    val logType: LogType,
    val timestamp: LocalTime = currentLocalTime(),
    val uuid: Uuid = Uuid.random()
)