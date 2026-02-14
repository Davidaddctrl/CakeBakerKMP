package com.davidlukash.cakebaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidlukash.cakebaker.data.UIActions
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.save.SaveFile
import com.davidlukash.cakebaker.repository.ResultSavesRepositoryWrapper
import com.davidlukash.cakebaker.repository.SavesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SaveFileViewModel(
    val uiActions: UIActions,
    _savesRepository: SavesRepository,
) : ViewModel() {
    private val savesRepository = ResultSavesRepositoryWrapper(_savesRepository)
    private val _saves = MutableStateFlow(emptyList<SaveFile>())
    val saves = _saves.asStateFlow()

    suspend fun listSavesSuspend(): Result<List<SaveFile>> {
       return savesRepository.listSaves().onSuccess {
           _saves.emit(it)
       }
    }

    fun listSaves(onResult: (Result<List<SaveFile>>) -> Unit = {}) {
        viewModelScope.launch {
            onResult(
                listSavesSuspend().onFailure {
                    uiActions.addTextPopup("Failed to list saves.")
                }
            )
        }
    }

    suspend fun deleteSave(name: String): Result<Boolean> = savesRepository.deleteSave(name)

    suspend fun upsertSave(file: SaveFile) = savesRepository.upsertSave(file)

    suspend fun exportSave(file: SaveFile): Result<Boolean> = savesRepository.exportSave(file)

    suspend fun importSave(): Result<Save?> = savesRepository.importSave()
}