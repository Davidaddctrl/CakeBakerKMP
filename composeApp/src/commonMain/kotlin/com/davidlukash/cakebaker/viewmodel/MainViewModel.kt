package com.davidlukash.cakebaker.viewmodel

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import com.davidlukash.cakebaker.repository.SavesRepository
import com.davidlukash.cakebaker.engine.CakeBakerEngine
import kotlin.uuid.ExperimentalUuidApi

class MainViewModel(
    savesRepository: SavesRepository,
) : ViewModel() {
    val themeViewModel = ThemeViewModel()

    val engine = CakeBakerEngine()

    @OptIn(ExperimentalUuidApi::class)
    val uiViewModel = UIViewModel()

    val saveFileViewModel = SaveFileViewModel(uiViewModel, savesRepository)
    val dataViewModel = DataViewModel(
        uiActions = uiViewModel,
        engine = engine
    )

    private var started = false

    fun start() {
        if (started) return
        started = true

        engine.initialize(uiViewModel)
        dataViewModel.initialize()
    }
}

val LocalMainViewModel = compositionLocalOf<MainViewModel> { throw Exception("No LocalMainViewModel provided") }
val LocalViewModelProvided = compositionLocalOf<Boolean> { false }