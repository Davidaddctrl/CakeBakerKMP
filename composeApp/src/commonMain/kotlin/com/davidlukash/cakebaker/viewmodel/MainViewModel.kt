package com.davidlukash.cakebaker.viewmodel

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val engine = CakeBakerEngine(this).also {
        it.registerStandardFunctions()
    }
    val uiViewModel = UIViewModel()
    val themeViewModel = ThemeViewModel()
    val dataViewModel = DataViewModel(uiViewModel, engine)

}

val LocalMainViewModel = compositionLocalOf<MainViewModel> { throw Exception("No LocalMainViewModel provided") }