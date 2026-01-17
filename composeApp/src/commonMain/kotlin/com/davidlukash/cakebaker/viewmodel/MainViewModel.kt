package com.davidlukash.cakebaker.viewmodel

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import com.davidlukash.cakebaker.data.ConsoleType
import com.davidlukash.cakebaker.data.Log
import com.davidlukash.cakebaker.data.LogType
import com.davidlukash.cakebaker.data.Save
import com.davidlukash.cakebaker.data.SaveFile
import com.davidlukash.cakebaker.data.SavesRepository
import com.davidlukash.cakebaker.dumpFunctionsToFile
import com.davidlukash.jsonmath.createNullObject
import com.davidlukash.jsonmath.createObject
import com.davidlukash.jsonmath.data.ObjectType
import com.davidlukash.jsonmath.engine.normal.ArgumentDescriptor
import com.davidlukash.jsonmath.engine.normal.FunctionDescriptor
import kotlin.uuid.ExperimentalUuidApi

open class MainViewModel(
    val savesRepository: SavesRepository,
) : ViewModel() {
    open val themeViewModel = ThemeViewModel()
    val engine = CakeBakerEngine(this).also {
        it.registerStandardFunctions()
        dumpFunctionsToFile(it)
    }
    @OptIn(ExperimentalUuidApi::class)
    open val uiViewModel = UIViewModel().also {
        engine.registerFunction(
            FunctionDescriptor(
                name = "console.mode",
                description = "Sets the mode of the debug console. Can be NONE, WINDOW, POPUP, or SIDEBAR",
                returnType = null,
                returnTypeNullable = true,
                arguments = listOf(
                    ArgumentDescriptor(name = "mode", type = ObjectType.STRING),
                )
            ) { args, _, _ ->
                it.setDebugConsole(ConsoleType.valueOf(args[0].asString()!!))
                createNullObject()
            }
        )
        engine.registerFunction(
            FunctionDescriptor(
                name = "console.internalShown",
                description = "Turns on or off the internal game runner",
                returnType = null,
                returnTypeNullable = true,
                arguments = listOf(
                    ArgumentDescriptor(name = "shown", type = ObjectType.BOOLEAN),
                )
            ) { args, _, _ ->
                it.setInternalShown(args[0].asBoolean()!!)
                createNullObject()
            }
        )
        val functionDump = engine.getAllFunctions().joinToString("\n\n") { engine.describeFunction(it) }
        it.appendLog(Log("Welcome to JsonMath 1.0.8", LogType.MESSAGE))
        it.appendLog(Log("List of all available functions:\n$functionDump", LogType.MESSAGE))
    }
    open val dataViewModel = DataViewModel(uiViewModel, engine).also {
        it.loadSave(Save.default)
    }
    open val saveFileViewModel = SaveFileViewModel(uiViewModel, savesRepository)

    fun createCrashSave() {
        var name = "crashsave"
        var i = 1
        var saves = savesRepository.listSaves().map { it.name }
        while (name in saves) {
            name = "crashsave${i++}"
            saves = savesRepository.listSaves().map { it.name }
        }
        savesRepository.upsertSave(SaveFile(name, dataViewModel.createSave()))
    }

}

val LocalMainViewModel = compositionLocalOf<MainViewModel> { throw Exception("No LocalMainViewModel provided") }
val LocalViewModelProvided = compositionLocalOf<Boolean> { false }