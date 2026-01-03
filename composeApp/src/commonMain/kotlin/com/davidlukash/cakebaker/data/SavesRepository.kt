package com.davidlukash.cakebaker.data

abstract class SavesRepository {
    abstract fun listSaves(): List<SaveFile>

    abstract fun deleteSave(name: String): Boolean

    abstract fun upsertSave(file: SaveFile): Boolean

    abstract fun exportSave(file: SaveFile): Boolean

    abstract fun importSave(): Boolean
}

expect val savesRepository: com.davidlukash.cakebaker.data.SavesRepository