package com.davidlukash.cakebaker.ui.screens.themescreen

interface ThemeDialogState {
    data object None : ThemeDialogState
    data class Delete(val theme: String) : ThemeDialogState
}