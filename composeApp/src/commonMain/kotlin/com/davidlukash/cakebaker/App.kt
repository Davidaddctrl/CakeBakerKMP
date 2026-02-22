package com.davidlukash.cakebaker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.memory.MemoryCache
import com.davidlukash.cakebaker.data.ConsoleType
import com.davidlukash.cakebaker.data.save.SaveFile
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.LocalTheme
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.ThemeFile
import com.davidlukash.cakebaker.ui.DebugPopup
import com.davidlukash.cakebaker.ui.DebugSideBar
import com.davidlukash.cakebaker.ui.ScaleViewport
import com.davidlukash.cakebaker.ui.navigation.KitchenScreen
import com.davidlukash.cakebaker.ui.navigation.Navigation
import com.davidlukash.cakebaker.ui.navigation.transitionDuration
import com.davidlukash.cakebaker.ui.screens.savescreen.CreateSaveDialog
import com.davidlukash.cakebaker.ui.screens.themescreen.ImportThemeDialog
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.viewmodel.LocalViewModelProvided
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi

const val VERSION = "Beta 0.9.2"
const val VERSIONCODE = 1

fun versionCodeToString(versionCode: Int?): String {
    return if (versionCode == null) "Unknown" else when (versionCode) {
        0 -> "Beta 0.9.1"
        1 -> "Beta 0.9.2"
        else -> "Unknown"
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.5)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .build()
    }
    CompositionLocalProvider(
        LocalViewModelProvided provides true
    ) {
        val mainViewModel = LocalMainViewModel.current

        LaunchedEffect(Unit) {
            mainViewModel.start()
        }

        val density = LocalDensity.current
        val uiViewModel = mainViewModel.uiViewModel
        val dataViewModel = mainViewModel.dataViewModel
        val themeViewModel = mainViewModel.themeViewModel
        val saveFileViewModel = mainViewModel.saveViewModel

        val coroutineScope = rememberCoroutineScope()

        val uiState by dataViewModel.uiStateFlow.collectAsState(UIState.default)
        val debugConsole by uiViewModel.debugConsole.collectAsState()
        val pendingScreen by uiViewModel.pendingScreen.collectAsState()
        val popups by uiViewModel.popups.collectAsState()
        val saveFiles by saveFileViewModel.saves.collectAsState()
        val importSaveDialogOpen by uiViewModel.importSaveDialogOpen.collectAsState()
        val importSaveData by uiViewModel.importSaveData.collectAsState()
        val importThemeDialogOpen by uiViewModel.importThemeDialogOpen.collectAsState()
        val importThemeData by uiViewModel.importThemeData.collectAsState()
        val theme by themeViewModel.theme.collectAsState()
        val themeFiles by themeViewModel.themes.collectAsState()
        val selectedThemes by themeViewModel.selectedThemes.collectAsState()
        val ovenProgress by dataViewModel.ovenProgress.collectAsState()
        val ovenRunning by dataViewModel.ovenRunning.collectAsState()
        val nextOrderRemainingTime by dataViewModel.nextOrderRemainingTime.collectAsState(initial = null)
        val orders by dataViewModel.orders.collectAsState()

        LaunchedEffect(density) {
            mainViewModel.uiViewModel.updateTrueDensity(density)
        }

        CompositionLocalProvider(
            LocalTheme provides theme
        ) {
            if (importSaveDialogOpen) {
                CreateSaveDialog(
                    exists = { name ->
                        saveFiles.map { it.name.uppercase() }.contains(name.uppercase())
                    },
                    create = { name ->
                        coroutineScope.launch {
                            importSaveData?.let { importSaveData ->
                                saveFileViewModel.listSavesSuspend().onSuccess {
                                    saveFileViewModel.upsertSave(SaveFile(name, importSaveData))
                                        .onSuccess {
                                            uiViewModel.setImportSaveData(null)
                                            uiViewModel.setImportSaveDialogOpen(false)
                                            uiViewModel.addTextPopup("Save Imported")
                                            saveFileViewModel.listSavesSuspend()
                                        }
                                        .onFailure {
                                            uiViewModel.setImportSaveData(null)
                                            uiViewModel.setImportSaveDialogOpen(false)
                                            uiViewModel.addTextPopup("Failed to import save")
                                        }
                                }
                            }
                        }
                    },
                    cancel = {
                        uiViewModel.setImportSaveData(null)
                        uiViewModel.setImportSaveDialogOpen(false)
                    },
                    isImport = true,
                )
            }
            if (importThemeDialogOpen) {
                ImportThemeDialog(
                    exists = { name ->
                        themeFiles.map { it.name.uppercase() }.contains(name.uppercase())
                    },
                    import = { name ->
                        coroutineScope.launch {
                            importThemeData?.let { importThemeData ->
                                themeViewModel.listThemesSuspend().onSuccess {
                                    themeViewModel.upsertTheme(ThemeFile(name, importThemeData))
                                        .onSuccess {
                                            uiViewModel.setImportThemeData(null)
                                            uiViewModel.setImportThemeDialogOpen(false)
                                            uiViewModel.addTextPopup("Theme Imported")
                                            themeViewModel.listThemesSuspend()
                                        }
                                        .onFailure {
                                            uiViewModel.setImportThemeData(null)
                                            uiViewModel.setImportThemeDialogOpen(false)
                                            uiViewModel.addTextPopup("Failed to import theme")
                                        }
                                }
                            }
                        }
                    },
                    cancel = {
                        uiViewModel.setImportThemeData(null)
                        uiViewModel.setImportThemeDialogOpen(false)
                    },
                )
            }

            Row(
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxSize(),
                ) {
                    ScaleViewport(1920.dp, 1280.dp) {
                        Navigation(
                            uiState = uiState,
                            pendingScreen = pendingScreen,
                            saveFiles = saveFiles,
                            popups = popups,
                            removePopup = { uiViewModel.removePopup(it) },
                            updateCurrentScreen = { uiViewModel.updateCurrentScreen(it) },
                            navigateWithFade = { uiViewModel.navigateWithFade(it) },
                            bake = { dataViewModel.startBake() },
                            buyIngredient = { dataViewModel.buyIngredient(it) },
                            setAutoOvenEnabled = { dataViewModel.setAutoOvenEnabled(it) },
                            setAutoOrderCompleteEnabled = { dataViewModel.setAutoOrderCompleteEnabled(it) },
                            completeOrder = { dataViewModel.handleCompleteOrder(it) },
                            setCurrentCake = { dataViewModel.setCurrentCake(it) },
                            listSaves = { coroutineScope.launch { saveFileViewModel.listSavesSuspend() } },
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
                                            saveFileViewModel.listSavesSuspend()
                                        }.onFailure {
                                            uiViewModel.addTextPopup(
                                                "Failed to delete save \"${saveFile.name}\""
                                            )
                                        }
                                }
                            },
                            loadSave = { saveFile ->
                                coroutineScope.launch {
                                    saveFileViewModel.listSavesSuspend().onSuccess {
                                        dataViewModel.loadSave(saveFile.save)
                                        uiViewModel.navigateWithFade(KitchenScreen)
                                        delay(transitionDuration.toLong())
                                        uiViewModel.addTextPopup("Save Loaded")
                                    }
                                }
                            },
                            loadWithMigration = { saveFile ->
                                coroutineScope.launch {
                                    saveFileViewModel.listSavesSuspend().onSuccess {
                                        dataViewModel.loadSave(saveFileViewModel.migrateSave(saveFile.save))
                                        uiViewModel.navigateWithFade(KitchenScreen)
                                        delay(transitionDuration.toLong())
                                        uiViewModel.addTextPopup("Save Loaded")
                                    }
                                }
                            },
                            importSave = {
                                coroutineScope.launch {
                                    saveFileViewModel.importSave()
                                        .onSuccess { save ->
                                            if (save != null) {
                                                saveFileViewModel.listSavesSuspend().onSuccess {
                                                    uiViewModel.setImportSaveData(save)
                                                    uiViewModel.setImportSaveDialogOpen(true)
                                                }
                                            }
                                        }
                                        .onFailure {
                                            uiViewModel.addTextPopup("Failed to import save")
                                        }
                                }
                            },
                            overwriteSave = { saveFile ->
                                coroutineScope.launch {
                                    saveFileViewModel.listSavesSuspend().onSuccess {
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
                                            saveFileViewModel.listSavesSuspend()
                                        }.onFailure {
                                            val existsBefore = saveNames.contains(saveFile.name.uppercase())
                                            uiViewModel.addTextPopup(
                                                if (!existsBefore) "Failed to create save \"${saveFile.name}\""
                                                else "Failed to overwrite save \"${saveFile.name}\""
                                            )
                                        }
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
                            themes = themeFiles.map { it.name },
                            initialSelectedThemes = selectedThemes,
                            listThemes = {
                                coroutineScope.launch {
                                    themeViewModel.listSelectedThemesSuspend().onSuccess {
                                        themeViewModel.listThemesSuspend()
                                    }
                                }
                            },
                            exportTheme = { themeName ->
                                coroutineScope.launch {
                                    val themeFile =
                                        if (themeName != "default") themeFiles.find { it.name == themeName } else ThemeFile(
                                            "default",
                                            Theme.default.toJsonTheme()
                                        )
                                    if (themeFile != null)
                                        themeViewModel.exportTheme(themeFile)
                                            .onSuccess { exported ->
                                                if (exported) uiViewModel.addTextPopup("Theme \"${themeFile.name}\" Exported")
                                            }
                                            .onFailure {
                                                uiViewModel.addTextPopup("Failed to export theme \"${themeFile.name}\"")
                                            }
                                }
                            },
                            deleteTheme = { themeName ->
                                coroutineScope.launch {
                                    val themeFile = themeFiles.find { it.name == themeName }
                                    if (themeFile != null)
                                        themeViewModel.deleteTheme(themeFile.name)
                                            .onSuccess { exists ->
                                                uiViewModel.addTextPopup(
                                                    if (exists) "Theme \"${themeFile.name}\" Deleted"
                                                    else "Theme \"${themeFile.name}\" Does Not Exist"
                                                )
                                                themeViewModel.listThemesSuspend()
                                            }.onFailure {
                                                uiViewModel.addTextPopup(
                                                    "Failed to delete theme \"${themeFile.name}\""
                                                )
                                            }
                                }
                            },
                            applyThemes = { themes ->
                                coroutineScope.launch {
                                    themeViewModel.listThemesSuspend().onSuccess {
                                        themeViewModel.applySelectedThemes(themes)
                                    }
                                }
                            },
                            importTheme = {
                                coroutineScope.launch {
                                    themeViewModel.importTheme()
                                        .onSuccess { theme ->
                                            if (theme != null) {
                                                themeViewModel.listThemesSuspend().onSuccess {
                                                    uiViewModel.setImportThemeData(theme)
                                                    uiViewModel.setImportThemeDialogOpen(true)
                                                }
                                            }
                                        }
                                        .onFailure {
                                            uiViewModel.addTextPopup("Failed to import theme")
                                        }
                                }
                            },
                        )
                    }
                    if (debugConsole == ConsoleType.POPUP) DebugPopup()
                }
                if (debugConsole == ConsoleType.SIDEBAR) DebugSideBar()
            }
        }
    }
}