package com.davidlukash.cakebaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidlukash.cakebaker.client
import com.davidlukash.cakebaker.data.UIActions
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.ThemeFile
import com.davidlukash.cakebaker.data.theme.json.JsonTheme
import com.davidlukash.cakebaker.json
import com.davidlukash.cakebaker.repository.ResultThemesRepositoryWrapper
import com.davidlukash.cakebaker.repository.ThemesRepository
import com.davidlukash.cakebaker.withResultSuspend
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsBytes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

class ThemeViewModel(
    val uiActions: UIActions,
    _themesRepository: ThemesRepository,
) : ViewModel() {
    private val _theme = MutableStateFlow(Theme.default)
    val theme = _theme.asStateFlow()

    fun setTheme(theme: Theme) {
        viewModelScope.launch {
            _theme.emit(theme)
        }
    }

    private val themesRepository = ResultThemesRepositoryWrapper(_themesRepository)
    private val _themes = MutableStateFlow(emptyList<ThemeFile>())
    val themes = _themes.asStateFlow()

    private val _selectedThemes = MutableStateFlow(emptyList<String>())
    val selectedThemes = _selectedThemes.asStateFlow()

    suspend fun listThemesSuspend(addPopupOnFailure: Boolean = true): Result<List<ThemeFile>> {
        return themesRepository.listThemes().onSuccess {
            _themes.emit(it)
        }.let {
            if (addPopupOnFailure) it.onFailure {
                uiActions.addTextPopup("Failed to list themes.")
            } else it
        }
    }

    suspend fun listSelectedThemesSuspend(addPopupOnFailure: Boolean = true): Result<List<String>> {
        return themesRepository.listSelectedThemes().onSuccess {
            _selectedThemes.emit(it)
        }.let {
            if (addPopupOnFailure) it.onFailure {
                uiActions.addTextPopup("Failed to list selected themes.")
            } else it
        }
    }

    suspend fun deleteTheme(name: String): Result<Boolean> = themesRepository.deleteTheme(name)

    suspend fun upsertTheme(file: ThemeFile) = themesRepository.upsertTheme(file)

    suspend fun exportTheme(file: ThemeFile): Result<Boolean> = themesRepository.exportTheme(file)

    suspend fun importTheme(): Result<JsonTheme?> = themesRepository.importTheme()

    fun applySelectedThemes(selectedThemes: List<String>) {
        viewModelScope.launch {
            themesRepository.setSelectedThemes(selectedThemes)
            themesRepository.listSelectedThemes()
            val themesMap = themes.value.associateBy { it.name }
            var theme = Theme.default
            selectedThemes.reversed().forEach { themeName ->
                themesMap[themeName]?.let { theme = it.theme.toTheme(theme) }
            }
            setTheme(theme)
        }
    }

    fun initialize() {
        viewModelScope.launch {
            listThemesSuspend().onSuccess {
                listSelectedThemesSuspend().onSuccess {
                    applySelectedThemes(selectedThemes.value)
                }
            }
        }
    }

    private val _waitingForImport = MutableStateFlow(false)
    val waitingForImport = _waitingForImport.asStateFlow()

    private suspend fun _importThemeFromURL(url: String, name: String?, isFirst: Boolean) {
        withResultSuspend {
            if (isFirst) _waitingForImport.emit(true)
            val response = client.get(url)
            val responseString = response.bodyAsBytes().decodeToString()
            val responseJson = json.parseToJsonElement(responseString)
            val shouldDialog = responseJson is JsonObject && name == null
            if (responseJson is JsonArray) {
                val listURLS = json.decodeFromJsonElement<List<String>>(responseJson)
                val regex = Regex("[a-z0-9]")
                listURLS.forEach { thisURL ->
                    val name = thisURL.split("/").last { it.isNotBlank() }.split(".").first()
                        .filter { regex.matches(it.toString()) }
                    if (name.isNotBlank()) {
                        _importThemeFromURL(thisURL, name, false)
                    }
                }
            }
            if (responseJson is JsonObject) {
                val jsonTheme = json.decodeFromJsonElement<JsonTheme>(responseJson)
                if (name == null) {
                    _waitingForImport.emit(false)
                    uiActions.triggerThemeImport(jsonTheme)
                } else {
                    themesRepository.upsertTheme(ThemeFile(name, jsonTheme))
                }
            }
            if (isFirst && !shouldDialog) {
                _waitingForImport.emit(false)
                listThemesSuspend()
            }
        }.onFailure {
            if (isFirst) _waitingForImport.emit(false)
            uiActions.addTextPopup("Failed to import theme from URL \"$url\".")
        }
    }


    fun importThemeFromURL(url: String, name: String? = null, isFirst: Boolean = true) {
        viewModelScope.launch {
            _importThemeFromURL(url, name, isFirst)
        }
    }
}