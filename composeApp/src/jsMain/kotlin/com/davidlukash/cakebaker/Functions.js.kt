package com.davidlukash.cakebaker

import androidx.compose.ui.Alignment
import com.davidlukash.cakebaker.data.Platform

actual val platform: Platform = Platform.JS

fun Alignment.toClass(): String = when (this) {
    Alignment.TopStart -> "top-left"
    Alignment.TopCenter -> "top-center"
    Alignment.TopEnd -> "top-right"

    Alignment.CenterStart -> "center-left"
    Alignment.Center -> "center"
    Alignment.CenterEnd -> "center-right"

    Alignment.BottomStart -> "bottom-left"
    Alignment.BottomCenter -> "bottom-center"
    Alignment.BottomEnd -> "bottom-right"
    else -> "top-left"
}

actual val usingHTML: Boolean = true