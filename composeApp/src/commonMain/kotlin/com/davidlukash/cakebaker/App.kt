package com.davidlukash.cakebaker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.ConsoleType
import com.davidlukash.cakebaker.data.Save
import com.davidlukash.cakebaker.data.SaveFile
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.ui.DebugPopup
import com.davidlukash.cakebaker.ui.DebugSideBar
import com.davidlukash.cakebaker.ui.ScaleViewport
import com.davidlukash.cakebaker.ui.navigation.KitchenScreen
import com.davidlukash.cakebaker.ui.navigation.Navigation
import com.davidlukash.cakebaker.ui.navigation.transitionDuration
import com.davidlukash.cakebaker.ui.screens.savescreen.CreateSaveDialog
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.viewmodel.ViewModelProvided
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


const val VERSION = "Alpha"

@Composable
fun App() {
    CompositionLocalProvider(
        ViewModelProvided provides true
    ) {
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
        val pendingScreen by uiViewModel.pendingScreen.collectAsState()
        val popups by uiViewModel.popups.collectAsState()
        val saveFiles by saveFileViewModel.savesFlow.collectAsState(listOf())
        val trueDensity by uiViewModel.trueDensity.collectAsState()
        var importDialogOpen by remember { mutableStateOf(false) }
        var importSaveData by remember { mutableStateOf<Save?>(null) }
        LaunchedEffect(density) {
            mainViewModel.uiViewModel.updateTrueDensity(density)
        }

        LaunchedEffect(themeViewModel) {
            themeViewModel.setTheme(theme)
        }

        if (importDialogOpen) {
            CreateSaveDialog(
                theme = theme,
                exists = { name ->
                    saveFiles.map { it.name.uppercase() }.contains(name.uppercase())
                },
                create = {
                    saveFileViewModel.upsertSave(SaveFile(it, importSaveData!!))
                    importSaveData = null
                    importDialogOpen = false
                    uiViewModel.addTextPopup("Save Imported")
                },
                cancel = {
                    importDialogOpen = false
                    importSaveData = null
                },
                isImport = true,
            )
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
                        saveFiles = saveFiles,
                        pendingScreen = pendingScreen,
                        popups = popups, trueDensity = trueDensity ?: LocalDensity.current,
                        removePopup = { uiViewModel.removePopup(it) },
                        updateCurrentScreen = { uiViewModel.updateCurrentScreen(it) },
                        navigateWithFade = { uiViewModel.navigateWithFade(it) },
                        bake = { dataViewModel.bake() },
                        buyIngredient = { dataViewModel.buyIngredient(it) },
                        setAutoOvenEnabled = { dataViewModel.setAutoOvenEnabled(it) },
                        completeOrder = { dataViewModel.handleCompleteOrder(it) },
                        setCurrentCake = { dataViewModel.setCurrentCake(it) },
                        exportSave = { saveFile ->
                            val result = saveFileViewModel.exportSave(saveFile)
                            result.onSuccess { exported ->
                                if (exported) uiViewModel.addTextPopup("Save Exported")
                            }
                            result.onFailure {
                                uiViewModel.addTextPopup("Save Error. Check debug console")
                            }
                        },
                        deleteSave = { saveFile ->
                            saveFileViewModel.deleteSave(saveFile.name)
                            uiViewModel.addTextPopup("Save Deleted")
                        },
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
                            val saveNames = saveFiles.map { it.name.uppercase() }
                            result.onSuccess {
                                if (!saveNames.contains(saveFile.name.uppercase()))
                                    uiViewModel.addTextPopup("Save Created")
                                else
                                    uiViewModel.addTextPopup("Save Overwritten")
                            }
                            result.onFailure {
                                uiViewModel.addTextPopup("Save Error. Check debug console")
                            }
                        },
                        importSave = {
                            val result = saveFileViewModel.importSave()
                            result.onSuccess { save ->
                                if (save != null) {
                                    importSaveData = save
                                    importDialogOpen = true
                                }
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
}