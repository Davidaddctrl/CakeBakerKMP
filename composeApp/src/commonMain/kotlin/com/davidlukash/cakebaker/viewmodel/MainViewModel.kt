package com.davidlukash.cakebaker.viewmodel

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import com.davidlukash.cakebaker.data.Log
import com.davidlukash.cakebaker.data.LogType
import com.davidlukash.cakebaker.data.Save
import com.davidlukash.cakebaker.data.SavesRepository
import com.davidlukash.cakebaker.dumpFunctionsToFile
import kotlin.uuid.ExperimentalUuidApi

class MainViewModel(
    val savesRepository: SavesRepository,
) : ViewModel() {
    val engine = CakeBakerEngine(this).also {
        it.registerStandardFunctions()
        dumpFunctionsToFile(it)
    }
    @OptIn(ExperimentalUuidApi::class)
    val uiViewModel = UIViewModel().also {
        val functionDump = engine.getAllFunctions().joinToString("\n\n") { engine.describeFunction(it) }
        it.appendLog(Log("Welcome to JsonMath 1.0.8", LogType.MESSAGE))
        it.appendLog(Log("List of all available functions:\n$functionDump", LogType.MESSAGE))
    }
    val themeViewModel = ThemeViewModel()
    val dataViewModel = DataViewModel(uiViewModel, engine).also {
        it.loadSave(Save.default)
    }
    val saveFileViewModel = SaveFileViewModel(savesRepository)

}

val LocalMainViewModel = compositionLocalOf<MainViewModel> { throw Exception("No LocalMainViewModel provided") }