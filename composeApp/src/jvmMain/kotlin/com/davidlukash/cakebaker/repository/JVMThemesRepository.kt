package com.davidlukash.cakebaker.repository.theme

import com.davidlukash.cakebaker.data.theme.ThemeFile
import com.davidlukash.cakebaker.data.theme.json.JsonTheme
import com.davidlukash.cakebaker.json
import com.davidlukash.cakebaker.repository.ThemesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.swing.JFileChooser

class JVMThemesRepository(
    baseDirectory: File,
) : ThemesRepository() {
    val themeDirectory = baseDirectory.resolve("themes").also { it.mkdirs() }

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

    override suspend fun exportTheme(file: ThemeFile): Boolean = withContext(Dispatchers.IO) {
        val fileChooser = JFileChooser()
        val option = fileChooser.showSaveDialog(null)
        if (option == JFileChooser.APPROVE_OPTION) {
            val selectedFile = fileChooser.selectedFile
            selectedFile.writeText(json.encodeToString(file.theme))
            true
        } else false
    }

    override suspend fun importTheme(): JsonTheme? = withContext(Dispatchers.IO) {
        val fileChooser = JFileChooser()
        val option = fileChooser.showOpenDialog(null)
        if (option == JFileChooser.APPROVE_OPTION) {
            val selectedFile = fileChooser.selectedFile
            val theme = json.decodeFromString<JsonTheme>(selectedFile.readText())
            theme
        } else null
    }
}
