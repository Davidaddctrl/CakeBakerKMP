package com.davidlukash.cakebaker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.layout.Measured
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.ConsoleType
import com.davidlukash.cakebaker.data.save.SaveFile
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.LocalTheme
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.platformui.HorizontalArrangement
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.ui.Text
import com.davidlukash.cakebaker.ui.DebugPopup
import com.davidlukash.cakebaker.ui.DebugSideBar
import com.davidlukash.cakebaker.ui.InternalPopup
import com.davidlukash.cakebaker.ui.ScaleViewport
import com.davidlukash.cakebaker.ui.VariableView
import com.davidlukash.cakebaker.ui.container.Background
import com.davidlukash.cakebaker.ui.container.PrimaryContainer
import com.davidlukash.cakebaker.ui.container.SecondaryContainer
import com.davidlukash.cakebaker.ui.input.LargeThemedButton
import com.davidlukash.cakebaker.ui.navigation.KitchenScreen
import com.davidlukash.cakebaker.ui.navigation.Navigation
import com.davidlukash.cakebaker.ui.navigation.transitionDuration
import com.davidlukash.cakebaker.ui.screens.kitchenscreen.RecipePanel
import com.davidlukash.cakebaker.ui.screens.savescreen.CreateSaveDialog
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.viewmodel.LocalViewModelProvided
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi

const val VERSION = "Beta 0.9.2"
const val VERSIONCODE = 1

@OptIn(ExperimentalUuidApi::class)
@Composable
fun App() {
    CompositionLocalProvider(
        LocalViewModelProvided provides true
    ) {
        val mainViewModel = LocalMainViewModel.current
        val density = LocalDensity.current
        val uiViewModel = mainViewModel.uiViewModel
        val dataViewModel = mainViewModel.dataViewModel
        val themeViewModel = mainViewModel.themeViewModel
        val saveFileViewModel = mainViewModel.saveFileViewModel

        val coroutineScope = rememberCoroutineScope()

        val globalScope = dataViewModel.globalScope
        val uiState by dataViewModel.uiStateFlow.collectAsState(UIState.default)
        val debugConsole by uiViewModel.debugConsole.collectAsState()
        val pendingScreen by uiViewModel.pendingScreen.collectAsState()
        val popups by uiViewModel.popups.collectAsState()
        val saveFiles by saveFileViewModel.saves.collectAsState()
        val trueDensity by uiViewModel.trueDensity.collectAsState()
        val importDialogOpen by uiViewModel.importDialogOpen.collectAsState()
        val importSaveData by uiViewModel.importSaveData.collectAsState()
        val theme by themeViewModel.theme.collectAsState()
        val internalShown by uiViewModel.internalShown.collectAsState()
        val variableShown by uiViewModel.variableShown.collectAsState()
        val ovenProgress by dataViewModel.ovenProgress.collectAsState()
        val ovenRunning by dataViewModel.ovenRunning.collectAsState()
        val nextOrderRemainingTime by dataViewModel.nextOrderRemainingTime.collectAsState(initial = null)
        val orders by dataViewModel.orders.collectAsState()

        Background {
            RecipePanel(
                modifier = Modifier.size(450.dp, 630.dp),
                uiState = uiState,
            ) { dataViewModel.setCurrentCake(it) }
        }

        return@CompositionLocalProvider

        LaunchedEffect(density) {
            mainViewModel.uiViewModel.updateTrueDensity(density)
        }

        LaunchedEffect(themeViewModel) {
            themeViewModel.setTheme(Theme.default)
        }

        CompositionLocalProvider(
            LocalTheme provides theme
        ) {
            if (importDialogOpen) {
                CreateSaveDialog(
                    exists = { name ->
                        saveFiles.map { it.name.uppercase() }.contains(name.uppercase())
                    },
                    create = {
                        coroutineScope.launch {
                            importSaveData?.let { importSaveData ->
                                saveFileViewModel.upsertSave(SaveFile(it, importSaveData))
                                    .onSuccess {
                                        uiViewModel.setImportSaveData(null)
                                        uiViewModel.setImportDialogOpen(false)
                                        uiViewModel.addTextPopup("Save Imported")
                                    }
                                    .onFailure {
                                        uiViewModel.setImportSaveData(null)
                                        uiViewModel.setImportDialogOpen(false)
                                        uiViewModel.addTextPopup("Failed to import save")
                                    }
                            }
                        }
                    },
                    cancel = {
                        uiViewModel.setImportSaveData(null)
                        uiViewModel.setImportDialogOpen(false)
                    },
                    isImport = true,
                )
            }

            Row(
                modifier = androidx.compose.ui.Modifier.fillMaxSize(),
            ) {
                Box(
                    modifier = androidx.compose.ui.Modifier.weight(1f).fillMaxSize(),
                ) {
                    ScaleViewport(1920.dp, 1200.dp) {
                        Navigation(
                            uiState = uiState,
                            pendingScreen = pendingScreen,
                            saveFiles = saveFiles,
                            popups = popups,
                            trueDensity = trueDensity ?: LocalDensity.current,
                            removePopup = { uiViewModel.removePopup(it) },
                            updateCurrentScreen = { uiViewModel.updateCurrentScreen(it) },
                            navigateWithFade = { uiViewModel.navigateWithFade(it) },
                            bake = { dataViewModel.startBake() },
                            buyIngredient = { dataViewModel.buyIngredient(it) },
                            setAutoOvenEnabled = { dataViewModel.setAutoOvenEnabled(it) },
                            setAutoOrderCompleteEnabled = { dataViewModel.setAutoOrderCompleteEnabled(it) },
                            completeOrder = { dataViewModel.handleCompleteOrder(it) },
                            setCurrentCake = { dataViewModel.setCurrentCake(it) },
                            exportSave = { saveFile ->
                                coroutineScope.launch {
                                    saveFileViewModel.exportSave(saveFile)
                                        .onSuccess { exported ->
                                            if (exported) uiViewModel.addTextPopup("Save \"${saveFile.name}\" Exported")
                                        }
                                        .onFailure {
                                            uiViewModel.addTextPopup("Failed to export save \"${saveFile.name}\"")
                                        }
                                }
                            },
                            deleteSave = { saveFile ->
                                coroutineScope.launch {
                                    saveFileViewModel.deleteSave(saveFile.name)
                                        .onSuccess { exists ->
                                            uiViewModel.addTextPopup(
                                                if (exists) "Save \"${saveFile.name}\" Deleted"
                                                else "Save \"${saveFile.name}\" Does Not Exist"
                                            )
                                        }.onFailure {
                                            uiViewModel.addTextPopup(
                                                "Failed to delete save \"${saveFile.name}\""
                                            )
                                        }
                                }
                            },
                            loadSave = { saveFile ->
                                coroutineScope.launch {
                                    dataViewModel.loadSave(saveFile.save)
                                    uiViewModel.navigateWithFade(KitchenScreen)
                                    delay(transitionDuration.toLong())
                                    uiViewModel.addTextPopup("Save Loaded")
                                }
                            },
                            importSave = {
                                coroutineScope.launch {
                                    saveFileViewModel.importSave()
                                        .onSuccess { save ->
                                            if (save != null) {
                                                uiViewModel.setImportSaveData(save)
                                                uiViewModel.setImportDialogOpen(true)
                                            }
                                        }
                                        .onFailure {
                                            uiViewModel.addTextPopup("Failed to import save")
                                        }
                                }
                            },
                            overwriteSave = { saveFile ->
                                coroutineScope.launch {
                                    val saveNames = saveFiles.map { it.name.uppercase() }
                                    saveFileViewModel.upsertSave(
                                        saveFile.copy(
                                            save = dataViewModel.createSave()
                                        )
                                    ).onSuccess { existsBefore ->
                                        uiViewModel.addTextPopup(
                                            if (!existsBefore) "Save \"${saveFile.name}\" Created"
                                            else "Save \"${saveFile.name}\" Overwritten"
                                        )
                                    }.onFailure {
                                        val existsBefore = saveNames.contains(saveFile.name.uppercase())
                                        uiViewModel.addTextPopup(
                                            if (!existsBefore) "Failed to create save \"${saveFile.name}\""
                                            else "Failed to overwrite save \"${saveFile.name}\""
                                        )
                                    }
                                }
                            },
                            buyUpgrade = { dataViewModel.buyUpgrade(it) },
                            setDebugConsole = { uiViewModel.setDebugConsole(it) },
                            consoleType = debugConsole,
                            ovenProgress = ovenProgress,
                            ovenRunning = ovenRunning,
                            nextOrderRemainingTime = nextOrderRemainingTime,
                            orders = orders,
                        )
                    }
                    if (debugConsole == ConsoleType.POPUP) DebugPopup()
                    if (internalShown) InternalPopup()
                    if (variableShown) VariableView(globalScope)
                }
                if (debugConsole == ConsoleType.SIDEBAR) DebugSideBar()
            }
        }
    }
}