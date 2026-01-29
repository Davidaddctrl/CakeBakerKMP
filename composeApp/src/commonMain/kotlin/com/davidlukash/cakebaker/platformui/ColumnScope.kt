package com.davidlukash.cakebaker.platformui

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment

@Immutable
interface ColumnScope {
    @Stable
    fun Modifier.weight(weight: Float): Modifier

    @Stable
    fun Modifier.align(alignment: Alignment.Horizontal): Modifier
}