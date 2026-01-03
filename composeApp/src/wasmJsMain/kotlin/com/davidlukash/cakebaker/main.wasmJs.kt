package com.davidlukash.cakebaker

import com.davidlukash.cakebaker.data.SavesRepository
import com.davidlukash.cakebaker.data.WASMSavesRepository

actual fun createSavesRepository(): SavesRepository = WASMSavesRepository()