package com.davidlukash.cakebaker.repository

import com.davidlukash.cakebaker.data.theme.ThemeFile
import com.davidlukash.cakebaker.data.theme.json.JsonTheme
import com.davidlukash.cakebaker.withResultSuspend

class ResultThemesRepositoryWrapper(private val themesRepository: ThemesRepository) {
    suspend fun listThemes(): Result<List<ThemeFile>> = withResultSuspend { themesRepository.listThemes() }
    suspend fun deleteTheme(name: String): Result<Boolean> = withResultSuspend { themesRepository.deleteTheme(name) }
    suspend fun upsertTheme(file: ThemeFile): Result<Boolean> = withResultSuspend { themesRepository.upsertTheme(file) }
    suspend fun exportTheme(file: ThemeFile): Result<Boolean> = withResultSuspend { themesRepository.exportTheme(file) }
    suspend fun importTheme(): Result<JsonTheme?> = withResultSuspend { themesRepository.importTheme() }
    suspend fun listSelectedThemes(): Result<List<String>> = withResultSuspend { themesRepository.listSelectedThemes() }
    suspend fun setSelectedThemes(list: List<String>): Result<Unit> = withResultSuspend { themesRepository.setSelectedThemes(list) }
}