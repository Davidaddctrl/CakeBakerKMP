package com.davidlukash.cakebaker.viewmodel

import androidx.lifecycle.ViewModel
import com.davidlukash.cakebaker.data.SavesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class SaveFileViewModel(
    private val savesRepository: SavesRepository,
) : ViewModel() {

    val savesFlow = flow {
        while (true) {
            val saveFiles = savesRepository.listSaves()
            emit(saveFiles)
            delay(50)
        }
    }
}