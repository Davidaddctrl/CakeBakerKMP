package com.davidlukash.cakebaker

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.fromKeyword
import androidx.compose.ui.input.pointer.pointerHoverIcon
import org.jetbrains.skiko.Cursor

@OptIn(ExperimentalComposeUiApi::class)
actual fun Modifier.horizontalDragCursor(): Modifier {
    return this.pointerHoverIcon(PointerIcon.fromKeyword("e-resize"))
}
@OptIn(ExperimentalComposeUiApi::class)
actual fun Modifier.verticalDragCursor(): Modifier {
    return this.pointerHoverIcon(PointerIcon.fromKeyword("n-resize"))
}