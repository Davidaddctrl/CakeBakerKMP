package com.davidlukash.cakebaker.data

import com.davidlukash.cakebaker.json
import java.io.File

class JVMSavesRepository(
    baseDirectory: File,
) : SavesRepository() {
    val saveDirectory = baseDirectory.resolve("saves").also { it.mkdirs() }

    override fun listSaves(): List<SaveFile> {
        return saveDirectory.listFiles { it.isFile }.map {
            val text = it.readText()
            SaveFile(it.name, json.decodeFromString<Save>(text))
        }
    }

    override fun deleteSave(name: String): Boolean {
        val saveFile = saveDirectory.resolve(name)
        if (!saveFile.exists()) return false
        saveFile.delete()
        return true
    }

    override fun upsertSave(file: SaveFile): Boolean {
        val saveFile = saveDirectory.resolve(file.name)
        val existsBefore = saveFile.createNewFile()
        saveFile.writeText(json.encodeToString(file.save))
        return existsBefore
    }

    override fun exportSave(file: SaveFile): Boolean {
        TODO("Not yet implemented")
    }

    override fun importSave(): Boolean {
        TODO("Not yet implemented")
    }
}