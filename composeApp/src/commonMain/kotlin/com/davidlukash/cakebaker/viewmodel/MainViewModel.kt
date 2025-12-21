package com.davidlukash.cakebaker.viewmodel

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val engine = CakeBakerEngine(this)
    val uiViewModel = UIViewModel()
    val themeViewModel = ThemeViewModel()
    val dataViewModel = DataViewModel(uiViewModel)
}

val LocalMainViewModel = compositionLocalOf<MainViewModel> { throw Exception("No LocalMainViewModel provided") }