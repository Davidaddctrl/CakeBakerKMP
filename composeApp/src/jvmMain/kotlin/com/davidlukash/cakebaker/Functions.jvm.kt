package com.davidlukash.cakebaker

import androidx.compose.foundation.gestures.onDrag
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import java.awt.Cursor

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