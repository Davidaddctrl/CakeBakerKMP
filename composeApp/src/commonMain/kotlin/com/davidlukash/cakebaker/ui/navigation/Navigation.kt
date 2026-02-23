package com.davidlukash.cakebaker.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Popup
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.davidlukash.cakebaker.data.*
import com.davidlukash.cakebaker.data.order.Order
import com.davidlukash.cakebaker.data.save.SaveFile
import com.davidlukash.cakebaker.ui.container.Background
import com.davidlukash.cakebaker.ui.MessageManager
import com.davidlukash.cakebaker.ui.screens.cakescreen.CakeScreen
import com.davidlukash.cakebaker.ui.screens.ingredientscreen.IngredientScreen
import com.davidlukash.cakebaker.ui.screens.kitchenscreen.KitchenScreen
import com.davidlukash.cakebaker.ui.screens.menuscreen.MenuScreen
import com.davidlukash.cakebaker.ui.screens.otherscreen.OtherScreen
import com.davidlukash.cakebaker.ui.screens.savescreen.SaveScreen
import com.davidlukash.cakebaker.ui.screens.settingsscreen.SettingsScreen
import com.davidlukash.cakebaker.ui.screens.themescreen.ThemeScreen
import com.davidlukash.cakebaker.ui.screens.upgradescreen.UpgradeScreen
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

const val transitionDuration = 500

@OptIn(ExperimentalUuidApi::class)
@Composable
fun NormalScreenMessageManager(
    popups: List<Popup>,
    removePopup: (Uuid) -> Unit,
    lazyListState: LazyListState,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Popup(
            alignment = Alignment.BottomCenter,
        ) {
            MessageManager(
                popups = popups,
                removePopup = removePopup,
                lazyListState = lazyListState
            )
        }
        content()
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
fun Navigation(
    uiState: UIState,
    pendingScreen: Screen?,
    saveFiles: List<SaveFile>,
    popups: List<Popup>,
    removePopup: (Uuid) -> Unit,
    updateCurrentScreen: (Screen) -> Unit,
    navigateWithFade: (Screen) -> Unit,
    bake: () -> Unit, buyIngredient: (String) -> Unit,
    setAutoOvenEnabled: (Boolean) -> Unit,
    setAutoOrderCompleteEnabled: (Boolean) -> Unit,
    completeOrder: (Order) -> Unit,
    setCurrentCake: (Int) -> Unit,
    listSaves: () -> Unit,
    exportSave: (SaveFile) -> Unit,
    deleteSave: (SaveFile) -> Unit,
    loadSave: (SaveFile) -> Unit,
    loadWithMigration: (SaveFile) -> Unit,
    importSave: () -> Unit,
    overwriteSave: (SaveFile) -> Unit,
    buyUpgrade: (Upgrade) -> Unit,
    setDebugConsole: (ConsoleType) -> Unit,
    consoleType: ConsoleType,
    ovenProgress: Double,
    ovenRunning: Boolean,
    nextOrderRemainingTime: Double?,
    orders: List<Order>,
    themes: List<String>,
    initialSelectedThemes: List<String>,
    listThemes: () -> Unit,
    exportTheme: (String) -> Unit,
    deleteTheme: (String) -> Unit,
    applyThemes: (List<String>) -> Unit,
    importTheme: () -> Unit,
    importThemeFromURL: (String) -> Unit
) {
    val navController = rememberNavController()
    val lazyListState = rememberLazyListState()
    val backStack by navController.currentBackStackEntryAsState()
    val currentScreen by remember { derivedStateOf {
        allScreensMap[backStack?.destination?.route]
    } }
    var currentUpgradePage by remember { mutableStateOf("") }

    LaunchedEffect(currentScreen) {
        currentScreen?.let {
            updateCurrentScreen(it as Screen)
        }
    }

    LaunchedEffect(pendingScreen) {
        pendingScreen?.let {
            navController.navigate(it as Screen)
        }
    }
    val navGraph = navController.createGraph(startDestination = CakeScreen) {
        composable<IngredientScreen> {
            IngredientScreen(uiState, navigateWithFade, buyIngredient)
        }
        composable<CakeScreen> {
            CakeScreen(navigateWithFade)
        }
        composable<MenuScreen> {
            MenuScreen(navigateWithFade)
        }
        composable<SettingsScreen> {
            SettingsScreen(navigateWithFade, setDebugConsole, consoleType)
        }
        composable<KitchenScreen> {
            KitchenScreen(
                uiState,
                navigateWithFade,
                bake,
                setAutoOvenEnabled,
                setAutoOrderCompleteEnabled,
                completeOrder,
                setCurrentCake,
                ovenProgress,
                ovenRunning,
                nextOrderRemainingTime,
                orders,
            )
        }
        composable<UpgradeScreen> {
            UpgradeScreen(uiState, navigateWithFade, buyUpgrade, currentUpgradePage) { currentUpgradePage = it }
        }
        composable<SaveScreen> {
            SaveScreen(
                saveFiles,
                listSaves,
                navigateWithFade,
                exportSave,
                deleteSave,
                loadSave,
                loadWithMigration,
                overwriteSave,
                importSave
            )
        }
        composable<OtherScreen> {
            OtherScreen(navigateWithFade)
        }
        composable<ThemeScreen> {
            ThemeScreen(
                themes = themes,
                initialSelectedThemes = initialSelectedThemes,
                listThemes = listThemes,
                navigateWithFade = navigateWithFade,
                exportTheme = exportTheme,
                deleteTheme = deleteTheme,
                applyThemes = applyThemes,
                importTheme = importTheme,
                importThemeFromURL = importThemeFromURL,
            )
        }
    }
    NormalScreenMessageManager(
        popups = popups,
        removePopup = removePopup,
        lazyListState = lazyListState,
    ) {
        Background {
            NavHost(
                navController, navGraph,
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(transitionDuration)
                    )
                }, exitTransition = {
                    fadeOut(
                        animationSpec = tween(transitionDuration)
                    )
                }
            )
        }
    }
}