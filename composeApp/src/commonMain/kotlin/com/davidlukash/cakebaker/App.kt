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
import com.davidlukash.cakebaker.ui.DebugPopup
import com.davidlukash.cakebaker.ui.DebugSideBar
import com.davidlukash.cakebaker.ui.GameTheme
import com.davidlukash.cakebaker.ui.ScaleViewport
import com.davidlukash.cakebaker.ui.navigation.Navigation
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview


const val VERSION = "Alpha"

@Composable
@Preview
fun App() {
    val mainViewModel = LocalMainViewModel.current
    val uiViewModel = mainViewModel.uiViewModel
    val debugConsole by uiViewModel.debugConsole.collectAsState()
    Row(
        modifier = Modifier.fillMaxSize(),
    ) {
        val density = LocalDensity.current
        LaunchedEffect(density) {
            mainViewModel.uiViewModel.updateTrueDensity(density)
        }
        Box(
            modifier = Modifier.weight(1f).fillMaxSize(),
        ) {
            GameTheme {
                ScaleViewport(2000.dp, 1080.dp, doAspectRatio = false) {
                    Navigation()
                }
            }
            if (debugConsole == ConsoleType.POPUP) DebugPopup()
        }
        if (debugConsole == ConsoleType.SIDEBAR) DebugSideBar()
    }
}