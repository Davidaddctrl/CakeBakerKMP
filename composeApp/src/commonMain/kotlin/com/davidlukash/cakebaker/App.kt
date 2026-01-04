package com.davidlukash.cakebaker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.ConsoleType
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.ui.DebugPopup
import com.davidlukash.cakebaker.ui.DebugSideBar
import com.davidlukash.cakebaker.ui.ScaleViewport
import com.davidlukash.cakebaker.ui.navigation.Navigation
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel


const val VERSION = "Alpha"

@Composable
fun App() {
    val mainViewModel = LocalMainViewModel.current
    val uiViewModel = mainViewModel.uiViewModel
    val themeViewModel = mainViewModel.themeViewModel
    val theme = getDefaultTheme()
    val debugConsole by uiViewModel.debugConsole.collectAsState()
    val density = LocalDensity.current
    LaunchedEffect(density) {
        mainViewModel.uiViewModel.updateTrueDensity(density)
    }

    LaunchedEffect(themeViewModel) {
        themeViewModel.setTheme(theme)
    }

    Row(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.weight(1f).fillMaxSize(),
        ) {
            ScaleViewport(1920.dp, 1080.dp) {
                Navigation(theme = theme)
            }
            if (debugConsole == ConsoleType.POPUP) DebugPopup()
        }
        if (debugConsole == ConsoleType.SIDEBAR) DebugSideBar()
    }
}