package com.davidlukash.cakebaker.viewmodel

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import com.davidlukash.cakebaker.data.Log
import com.davidlukash.cakebaker.data.LogType
import com.davidlukash.cakebaker.dumpFunctionsToFile
import kotlin.uuid.ExperimentalUuidApi

class MainViewModel : ViewModel() {
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
    val dataViewModel = DataViewModel(uiViewModel, engine)

}

val LocalMainViewModel = compositionLocalOf<MainViewModel> { throw Exception("No LocalMainViewModel provided") }