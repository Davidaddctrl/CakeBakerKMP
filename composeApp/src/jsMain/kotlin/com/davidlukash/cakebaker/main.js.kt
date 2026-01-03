package com.davidlukash.cakebaker

import com.davidlukash.cakebaker.data.JSSavesRepository
import com.davidlukash.cakebaker.data.SavesRepository

actual fun createSavesRepository(): SavesRepository = JSSavesRepository()