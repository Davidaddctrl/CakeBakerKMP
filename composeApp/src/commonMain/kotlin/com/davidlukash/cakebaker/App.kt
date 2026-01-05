package com.davidlukash.cakebaker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.ConsoleType
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.ui.DebugPopup
import com.davidlukash.cakebaker.ui.DebugSideBar
import com.davidlukash.cakebaker.ui.ScaleViewport
import com.davidlukash.cakebaker.ui.navigation.KitchenScreen
import com.davidlukash.cakebaker.ui.navigation.Navigation
import com.davidlukash.cakebaker.ui.navigation.transitionDuration
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


const val VERSION = "Alpha"

@Composable
fun App() {
    val mainViewModel = LocalMainViewModel.current
    val uiViewModel = mainViewModel.uiViewModel
    val dataViewModel = mainViewModel.dataViewModel
    val themeViewModel = mainViewModel.themeViewModel
    val saveFileViewModel = mainViewModel.saveFileViewModel
    val theme = getDefaultTheme()
    val uiState by dataViewModel.uiStateFlow.collectAsState(UIState.default)
    val debugConsole by uiViewModel.debugConsole.collectAsState()
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
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
                Navigation(
                    theme = theme, uiState = uiState,
                    navigateWithFade = { uiViewModel.navigateTo(it) },
                    bake = { dataViewModel.bake() },
                    buyIngredient = { dataViewModel.buyIngredient(it) },
                    setAutoOvenEnabled = { dataViewModel.setAutoOvenEnabled(it) },
                    completeOrder = { dataViewModel.handleCompleteOrder(it) },
                    setCurrentCake = { dataViewModel.setCurrentCake(it) },
                    exportSave = { saveFile -> saveFileViewModel.exportSave(saveFile) },
                    deleteSave = { saveFile -> saveFileViewModel.deleteSave(saveFile.name) },
                    loadSave = { saveFile ->
                        coroutineScope.launch {
                            dataViewModel.loadSave(saveFile.save)
                            uiViewModel.navigateWithFade(KitchenScreen)
                            delay(transitionDuration.toLong())
                            uiViewModel.addTextPopup("Save Loaded")
                        }
                    },
                    overwriteSave = { saveFile ->
                        val result = withErrorHandling(uiViewModel) {
                            saveFileViewModel.upsertSave(
                                saveFile.copy(
                                    save = dataViewModel.createSave()
                                )
                            )
                        }
                        result.onSuccess {
                            uiViewModel.addTextPopup("Save Overwritten")
                        }
                        result.onFailure {
                            uiViewModel.addTextPopup("Save Error. Check debug console")
                        }
                    },
                    buyUpgrade = { dataViewModel.buyUpgrade(it) },
                )
            }
            if (debugConsole == ConsoleType.POPUP) DebugPopup()
        }
        if (debugConsole == ConsoleType.SIDEBAR) DebugSideBar()
    }
}