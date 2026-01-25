package com.davidlukash.cakebaker.logger

import com.davidlukash.cakebaker.data.log.Log

class CompoundAppLogger : AppLogger() {
    private val loggers = mutableSetOf<AppLogger>()

    override fun appendLog(log: Log) {
        loggers.forEach { logger ->
            try {
                logger.appendLog(log)
            } catch (_: Throwable) {
                //Never crash
            }
        }
    }

    fun registerLogger(logger: AppLogger) {
        loggers.add(logger)
    }

    fun unregisterLogger(logger: AppLogger) {
        loggers.remove(logger)
    }
}