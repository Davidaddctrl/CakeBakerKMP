package com.davidlukash.cakebaker.platformui

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment

@Immutable
interface BoxScope {
    @Stable
    fun Modifier.align(alignment: Alignment): Modifier
}