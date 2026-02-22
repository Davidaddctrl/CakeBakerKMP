package com.davidlukash.cakebaker.repository

import com.davidlukash.cakebaker.data.theme.ThemeFile
import com.davidlukash.cakebaker.data.theme.json.JsonTheme

abstract class ThemesRepository {
    abstract suspend fun listThemes(): List<ThemeFile>

    abstract suspend fun deleteTheme(name: String): Boolean

    abstract suspend fun upsertTheme(file: ThemeFile): Boolean

    abstract suspend fun exportTheme(file: ThemeFile): Boolean

    abstract suspend fun importTheme(): JsonTheme?

    abstract suspend fun listSelectedThemes(): List<String>

    abstract suspend fun setSelectedThemes(list: List<String>)
}