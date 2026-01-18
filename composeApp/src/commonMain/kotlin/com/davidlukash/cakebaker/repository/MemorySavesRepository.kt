package com.davidlukash.cakebaker.repository

import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.save.SaveFile

class MemorySavesRepository(
    val baseRepository: SavesRepository
) : SavesRepository() {

    var saves = listOf<SaveFile>()

    override fun listSaves(): List<SaveFile> = saves

    override fun deleteSave(name: String): Boolean {
        val result = saves.any { it.name == name }
        saves = saves.filter { it.name != name }
        return result
    }

    override fun upsertSave(file: SaveFile): Boolean {
        val existsBefore = saves.any { it.name == file.name }
        if (!existsBefore) saves = saves + file
        else {
            val index = saves.indexOfFirst { it.name == file.name }
            if (index != -1) {
                saves = (listOf(file) + saves).distinctBy { it.name }
            }
        }
        return existsBefore
    }

    override fun exportSave(file: SaveFile): Boolean {
        baseRepository.upsertSave(file)
        return true
    }

    override fun importSave(): Save? = null
}