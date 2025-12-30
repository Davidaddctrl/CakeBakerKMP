package com.davidlukash.cakebaker

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import android.view.PointerIcon as AndroidPointerIcon

actual fun Modifier.horizontalDragCursor(): Modifier {
    return this.pointerHoverIcon(PointerIcon(AndroidPointerIcon.TYPE_HORIZONTAL_DOUBLE_ARROW))
}
actual fun Modifier.verticalDragCursor(): Modifier {
    return this.pointerHoverIcon(PointerIcon(AndroidPointerIcon.TYPE_VERTICAL_DOUBLE_ARROW))
}