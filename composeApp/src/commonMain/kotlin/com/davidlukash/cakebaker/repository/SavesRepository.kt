package com.davidlukash.cakebaker.repository

import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.save.SaveFile

abstract class SavesRepository {
    abstract fun listSaves(): List<SaveFile>

    abstract fun deleteSave(name: String): Boolean

    abstract fun upsertSave(file: SaveFile): Boolean

    abstract fun exportSave(file: SaveFile): Boolean

    abstract fun importSave(): Save?
}