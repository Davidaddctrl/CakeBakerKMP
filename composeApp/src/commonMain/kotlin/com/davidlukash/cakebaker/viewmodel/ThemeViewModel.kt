package com.davidlukash.cakebaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidlukash.cakebaker.data.Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThemeViewModel : ViewModel() {
    private val _theme = MutableStateFlow(Theme.default)
    val theme = _theme.asStateFlow()

    fun setTheme(theme: Theme) {
        viewModelScope.launch {
            _theme.emit(theme)
        }
    }
}