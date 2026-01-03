package com.davidlukash.cakebaker.data

import com.davidlukash.cakebaker.json
import kotlinx.browser.window

class WASMSavesRepository : SavesRepository() {
    private val localStorage = window.localStorage

    private fun updateSaves(list: List<SaveFile>) {
        localStorage.setItem("saves", json.encodeToString(list))
    }

    override fun listSaves(): List<SaveFile> {
        val savesString = localStorage.getItem("saves") ?: "[]"
        val saves = json.decodeFromString<List<SaveFile>>(savesString)
        return saves
    }

    override fun deleteSave(name: String): Boolean {
        val saves = listSaves()
        if (!saves.map { it.name }.contains(name)) return false
        updateSaves(saves.filterNot { it.name == name })
        return true
    }

    override fun upsertSave(file: SaveFile): Boolean {
        val saves = listSaves()
        val existsBefore = saves.map { it.name }.contains(file.name)
        updateSaves((listOf(file) + saves).distinctBy { it.name })
        return existsBefore
    }

    override fun exportSave(file: SaveFile): Boolean {
        TODO("Not yet implemented")
    }

    override fun importSave(): Boolean {
        TODO("Not yet implemented")
    }
}