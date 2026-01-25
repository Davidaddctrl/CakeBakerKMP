package com.davidlukash.cakebaker.repository

import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.save.SaveFile
import com.davidlukash.cakebaker.withResult
import com.davidlukash.cakebaker.withResultSuspend

class ResultSavesRepositoryWrapper(private val savesRepository: SavesRepository) {
    suspend fun listSaves(): Result<List<SaveFile>> = withResultSuspend { savesRepository.listSaves() }
    suspend fun deleteSave(name: String): Result<Boolean> = withResultSuspend { savesRepository.deleteSave(name) }
    suspend fun upsertSave(file: SaveFile): Result<Boolean> = withResultSuspend { savesRepository.upsertSave(file) }
    suspend fun exportSave(file: SaveFile): Result<Boolean> = withResultSuspend { savesRepository.exportSave(file) }
    suspend fun importSave(): Result<Save?> = withResultSuspend { savesRepository.importSave() }
}