package com.davidlukash.cakebaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidlukash.cakebaker.data.SaveFile
import com.davidlukash.cakebaker.data.SavesRepository
import com.davidlukash.cakebaker.withErrorHandling
import com.davidlukash.cakebaker.withErrorHandlingAsync
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class SaveFileViewModel(
    val uiViewModel: UIViewModel,
    private val savesRepository: SavesRepository,
) : ViewModel() {
    var shouldRetry = true

    val savesFlow = flow {
        while (true) {
            if (shouldRetry) {
                val result = withErrorHandlingAsync(uiViewModel) {
                    val saveFiles = savesRepository.listSaves()
                    emit(saveFiles)
                    true
                }
                result.onFailure {
                    shouldRetry = false
                    uiViewModel.addTextButtonPopup("Save Error. Check debug console", false, "Retry") {
                        shouldRetry = true
                    }
                    emit(emptyList())
                }
            }
            delay(50)

        }
    }

    fun deleteSave(name: String) {
        viewModelScope.launch {
            withErrorHandling(uiViewModel) {
                savesRepository.deleteSave(name)
            }
        }
    }

    fun upsertSave(file: SaveFile) {
        viewModelScope.launch {
            withErrorHandling(uiViewModel) {
                savesRepository.upsertSave(file)
            }
        }
    }

    fun exportSave(file: SaveFile) {
        viewModelScope.launch {
            withErrorHandling(uiViewModel) {
                val created = savesRepository.exportSave(file)
                if (created) uiViewModel.addTextPopup("Save Exported")
            }
        }
    }

    fun importSave() {
        viewModelScope.launch {
            withErrorHandling(uiViewModel) {
                savesRepository.importSave()
            }
        }
    }
}