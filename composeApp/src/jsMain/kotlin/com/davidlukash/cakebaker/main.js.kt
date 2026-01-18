package com.davidlukash.cakebaker

import com.davidlukash.cakebaker.data.JSSavesRepository
import com.davidlukash.cakebaker.repository.SavesRepository

actual fun createSavesRepository(): SavesRepository = JSSavesRepository()