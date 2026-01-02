package com.davidlukash.cakebaker

import com.davidlukash.cakebaker.data.ConsoleType
import com.davidlukash.cakebaker.data.Log

interface AppLogger {
    fun appendLog(log: Log)

    fun setDebugConsole(type: ConsoleType)

    fun getDebugConsole(): ConsoleType
}