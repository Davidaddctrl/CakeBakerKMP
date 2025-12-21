package com.davidlukash.cakebaker

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.viewmodel.MainViewModel

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        val localMainViewModel = remember { MainViewModel() }
        CompositionLocalProvider(
            LocalMainViewModel provides localMainViewModel
        ) {
            App()
        }
    }
}