package com.davidlukash.cakebaker

import com.davidlukash.cakebaker.repository.SavesRepository
import com.davidlukash.cakebaker.data.WASMSavesRepository

actual fun createSavesRepository(): SavesRepository = WASMSavesRepository()