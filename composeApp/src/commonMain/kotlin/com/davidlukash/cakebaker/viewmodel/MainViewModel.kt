package com.davidlukash.cakebaker.viewmodel

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import com.davidlukash.cakebaker.data.ConsoleType
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.repository.SavesRepository
import com.davidlukash.cakebaker.dumpFunctionsToFile
import com.davidlukash.cakebaker.engine.CakeBakerEngine
import com.davidlukash.cakebaker.logger
import com.davidlukash.jsonmath.createNullObject
import com.davidlukash.jsonmath.createObject
import com.davidlukash.jsonmath.data.ObjectType
import com.davidlukash.jsonmath.engine.normal.ArgumentDescriptor
import com.davidlukash.jsonmath.engine.normal.FunctionDescriptor
import kotlinx.coroutines.launch
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
                returnType = ObjectType.BOOLEAN,
                returnTypeNullable = false,
                arguments = listOf(
                    ArgumentDescriptor(name = "shown", type = ObjectType.BOOLEAN, optional = true, nullable = true),
                )
            ) { args, _, _ ->
                it.setInternalShown(args[0].asNullableBoolean("") ?: it.internalShown.value)
                createObject(it.internalShown.value)
            }
        )
        engine.registerFunction(
            FunctionDescriptor(
                name = "console.variableShown",
                description = "Turns on or off the variable view",
                returnType = ObjectType.BOOLEAN,
                returnTypeNullable = false,
                arguments = listOf(
                    ArgumentDescriptor(name = "shown", type = ObjectType.BOOLEAN, optional = true, nullable = true),
                )
            ) { args, _, _ ->
                it.setVariableShown(args[0].asNullableBoolean("") ?: it.variableShown.value)
                createObject(it.variableShown.value)
            }
        )
        val functionDump = engine.getAllFunctions().joinToString("\n\n") { engine.describeFunction(it) }
        logger.logInfo("Welcome to JsonMath 1.0.8")
        logger.logDebug("List of all available functions:\n$functionDump")
    }
    open val dataViewModel = DataViewModel(uiViewModel, engine).also {
        it.loadSave(Save.default)
    }
    open val saveFileViewModel = SaveFileViewModel(uiViewModel, savesRepository)
}

val LocalMainViewModel = compositionLocalOf<MainViewModel> { throw Exception("No LocalMainViewModel provided") }
val LocalViewModelProvided = compositionLocalOf<Boolean> { false }