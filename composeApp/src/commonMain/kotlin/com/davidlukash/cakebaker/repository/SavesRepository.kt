package com.davidlukash.cakebaker.repository

import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.save.SaveFile

abstract class SavesRepository {
    abstract suspend fun listSaves(): List<SaveFile>

    abstract suspend fun deleteSave(name: String): Boolean

    abstract suspend fun upsertSave(file: SaveFile): Boolean

    abstract suspend fun exportSave(file: SaveFile): Boolean

    abstract suspend fun importSave(): Save?
}