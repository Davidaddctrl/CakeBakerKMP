package com.davidlukash.cakebaker.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Popup

@Composable
fun MeasureLayout(onMeasured: (IntSize) -> Unit, content: @Composable () -> Unit) {
    Popup {
        Layout(
            content = content
        ) { measurables, constraints ->
            val placeables = measurables.map { it.measure(constraints) }

            val width = placeables.maxOfOrNull { it.width } ?: 0
            val height = placeables.maxOfOrNull { it.height } ?: 0

            onMeasured(IntSize(width, height))

            layout(0, 0) {}
        }
    }
}