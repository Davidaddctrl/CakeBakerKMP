package com.davidlukash.cakebaker

import androidx.compose.foundation.gestures.onDrag
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import com.davidlukash.cakebaker.data.Log
import com.davidlukash.cakebaker.data.LogType
import com.davidlukash.cakebaker.data.baseDirectory
import com.davidlukash.cakebaker.viewmodel.CakeBakerEngine
import java.awt.Cursor
import kotlin.uuid.ExperimentalUuidApi

actual fun Modifier.horizontalDragCursor(): Modifier {
    return this.pointerHoverIcon(
        PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR))
    )
}

actual fun Modifier.verticalDragCursor(): Modifier {
    return this.pointerHoverIcon(
        PointerIcon(Cursor(Cursor.N_RESIZE_CURSOR))
    )
}

@OptIn(ExperimentalUuidApi::class)
actual fun dumpFunctionsToFile(
    engine: CakeBakerEngine
) {
    val functionDump = engine.getAllFunctions().joinToString("\n\n") { engine.describeFunction(it) }
    baseDirectory.resolve("functions.txt").also {
        it.createNewFile()
    }.writeText(functionDump)
}