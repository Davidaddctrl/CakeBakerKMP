package com.davidlukash.cakebaker.data

import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.save.SaveFile
import com.davidlukash.cakebaker.json
import com.davidlukash.cakebaker.repository.SavesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.swing.JFileChooser

class JVMSavesRepository(
    baseDirectory: File,
) : SavesRepository() {
    val saveDirectory = baseDirectory.resolve("saves").also { it.mkdirs() }

    override suspend fun listSaves(): List<SaveFile> = withContext(Dispatchers.IO) {
        saveDirectory.listFiles { it.isFile }.map {
            val text = it.readText()
            SaveFile(it.name, json.decodeFromString<Save>(text))
        }
    }

    override suspend fun deleteSave(name: String): Boolean = withContext(Dispatchers.IO) {
        val saveFile = saveDirectory.resolve(name)
        if (!saveFile.exists()) return@withContext false
        saveFile.delete()
        return@withContext true
    }

    override suspend fun upsertSave(file: SaveFile): Boolean = withContext(Dispatchers.IO) {
        val saveFile = saveDirectory.resolve(file.name)
        val existsBefore = saveFile.createNewFile()
        saveFile.writeText(json.encodeToString(file.save))
        existsBefore
    }

    override suspend fun exportSave(file: SaveFile): Boolean = withContext(Dispatchers.IO) {
        val fileChooser = JFileChooser()
        val option = fileChooser.showSaveDialog(null)
        if (option == JFileChooser.APPROVE_OPTION) {
            val selectedFile = fileChooser.selectedFile
            selectedFile.writeText(json.encodeToString(file.save))
            true
        } else false
    }

    override suspend fun importSave(): Save? = withContext(Dispatchers.IO) {
        val fileChooser = JFileChooser()
        val option = fileChooser.showOpenDialog(null)
        if (option == JFileChooser.APPROVE_OPTION) {
            val selectedFile = fileChooser.selectedFile
            val save = json.decodeFromString<Save>(selectedFile.readText())
            save
        } else null
    }
}