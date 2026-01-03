package com.davidlukash.cakebaker.data

import com.davidlukash.cakebaker.json
import kotlinx.serialization.builtins.serializer
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.Path

val baseDirectory = File(".").absoluteFile.resolve("CakeBaker").also { it.mkdirs() }

actual val savesRepository: SavesRepository = object : SavesRepository() {
    val saveDirectory = baseDirectory.resolve("saves")

    override fun listSaves(): List<SaveFile> {
        return saveDirectory.listFiles { it.isFile }.map {
            val text = it.readText()
            json.decodeFromString<SaveFile>(text)
        }
    }

    override fun deleteSave(name: String): Boolean {
        val saveFile = saveDirectory.resolve(name)
        if (!saveFile.exists()) return false
        saveFile.delete()
        return true
    }

    override fun upsertSave(file: SaveFile): Boolean {
        return false
    }

    override fun exportSave(file: SaveFile): Boolean {
        return false
    }

    override fun importSave(): Boolean {
        return false
    }
}