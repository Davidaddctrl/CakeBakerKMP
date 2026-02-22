package com.davidlukash.cakebaker.repository

import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat.startActivityForResult
import com.davidlukash.cakebaker.MainActivity
import com.davidlukash.cakebaker.data.theme.ThemeFile
import com.davidlukash.cakebaker.data.theme.json.JsonTheme
import com.davidlukash.cakebaker.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

const val EXPORT_THEME = 3
const val IMPORT_THEME = 4


class AndroidThemesRepository(
    baseDirectory: File,
    val activity: MainActivity
) : ThemesRepository() {
    val themeDirectory = baseDirectory.resolve("themes").also { it.mkdirs() }
    val selectedThemesFile = baseDirectory.resolve("selected_themes.json").also { if (it.createNewFile()) it.writeText("[]") }

    override suspend fun listThemes(): List<ThemeFile> = withContext(Dispatchers.IO) {
        themeDirectory.listFiles { it.isFile }.map {
            val text = it.readText()
            ThemeFile(it.name, json.decodeFromString<JsonTheme>(text))
        }
    }

    override suspend fun deleteTheme(name: String): Boolean = withContext(Dispatchers.IO) {
        val themeFile = themeDirectory.resolve(name)
        if (!themeFile.exists()) return@withContext false
        themeFile.delete()
        return@withContext true
    }

    override suspend fun upsertTheme(file: ThemeFile): Boolean = withContext(Dispatchers.IO) {
        val themeFile = themeDirectory.resolve(file.name)
        val existsBefore = !themeFile.createNewFile()
        themeFile.writeText(json.encodeToString(file.theme))
        existsBefore
    }

    override suspend fun exportTheme(file: ThemeFile): Boolean {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
            putExtra(Intent.EXTRA_TITLE, "${file.name}.json")
        }

        activity.themeToBeExported = file.theme
        startActivityForResult(activity, intent, EXPORT_THEME, Bundle())

        return false
    }


    override suspend fun importTheme(): JsonTheme? {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
        }

        startActivityForResult(activity, intent, IMPORT_THEME, Bundle())

        return null
    }

    override suspend fun listSelectedThemes(): List<String> = withContext(Dispatchers.IO) {
        json.decodeFromString<List<String>>(selectedThemesFile.readText())
    }

    override suspend fun setSelectedThemes(list: List<String>) = withContext(Dispatchers.IO) {
        selectedThemesFile.writeText(json.encodeToString(list))
    }
}
