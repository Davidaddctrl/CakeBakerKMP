package com.davidlukash.cakebaker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.ui.DebugConsole
import com.davidlukash.cakebaker.ui.GameTheme
import com.davidlukash.cakebaker.ui.ScaleViewport
import com.davidlukash.cakebaker.ui.navigation.Navigation
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

const val VERSION = "Alpha"


const val usingDebugSidebar = false

@Composable
@Preview
fun App() {
    val mainViewModel = LocalMainViewModel.current
    Row(
        modifier = Modifier.fillMaxSize(),
    ) {
        val density = LocalDensity.current
        LaunchedEffect(density) {
            mainViewModel.uiViewModel.updateTrueDensity(density)
        }
        Box(
            modifier = Modifier.weight(3f).fillMaxSize(),
        ) {
            GameTheme {
                ScaleViewport(2000.dp, 1080.dp, doAspectRatio = false) {
                    Navigation()
                }
            }
        }
        if (usingDebugSidebar) DebugConsole()
    }
}