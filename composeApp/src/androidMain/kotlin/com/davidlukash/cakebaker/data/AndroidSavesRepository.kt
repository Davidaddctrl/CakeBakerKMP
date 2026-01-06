package com.davidlukash.cakebaker.data

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat.startActivityForResult
import com.davidlukash.cakebaker.MainActivity
import com.davidlukash.cakebaker.json
import java.io.File

const val EXPORT_SAVE = 1
const val IMPORT_SAVE = 2

class AndroidSavesRepository(
    baseDirectory: File,
    val activity: MainActivity
) : SavesRepository() {
    val saveDirectory = baseDirectory.resolve("saves").also { it.mkdirs() }


    override fun listSaves(): List<SaveFile> {
        return saveDirectory.listFiles { it.isFile }?.map {
            val text = it.readText()
            SaveFile(it.name, json.decodeFromString<Save>(text))
        } ?: emptyList()
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
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
            putExtra(Intent.EXTRA_TITLE, "${file.name}.json")
        }

        activity.saveToBeExported = file.save
        startActivityForResult(activity, intent, EXPORT_SAVE, Bundle())

        return false
    }

    override fun importSave(): Save? {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
        }

        startActivityForResult(activity, intent, IMPORT_SAVE, Bundle())

        return null
    }
}
