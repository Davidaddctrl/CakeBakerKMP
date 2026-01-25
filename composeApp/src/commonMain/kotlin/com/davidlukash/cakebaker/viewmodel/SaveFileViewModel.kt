package com.davidlukash.cakebaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.save.SaveFile
import com.davidlukash.cakebaker.repository.ResultSavesRepositoryWrapper
import com.davidlukash.cakebaker.repository.SavesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SaveFileViewModel(
    val uiViewModel: UIViewModel,
    private val _savesRepository: SavesRepository,
) : ViewModel() {
    private val savesRepository = ResultSavesRepositoryWrapper(_savesRepository)
    private val _saves = MutableStateFlow(emptyList<SaveFile>())
    val saves = _saves.asStateFlow()

    suspend fun listSavesSuspend(): Result<List<SaveFile>> {
       return savesRepository.listSaves().onSuccess {
           _saves.emit(it)
       }
    }

    suspend fun deleteSave(name: String): Result<Boolean> = savesRepository.deleteSave(name)

    suspend fun upsertSave(file: SaveFile) = savesRepository.upsertSave(file)

    suspend fun exportSave(file: SaveFile): Result<Boolean> = savesRepository.exportSave(file)

    suspend fun importSave(): Result<Save?> = savesRepository.importSave()
}