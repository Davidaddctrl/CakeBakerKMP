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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.window.Popup
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.davidlukash.cakebaker.data.*
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.ui.Background
import com.davidlukash.cakebaker.ui.MessageManager
import com.davidlukash.cakebaker.ui.screens.cakescreen.CakeScreen
import com.davidlukash.cakebaker.ui.screens.ingredientscreen.IngredientScreen
import com.davidlukash.cakebaker.ui.screens.kitchenscreen.KitchenScreen
import com.davidlukash.cakebaker.ui.screens.menuscreen.MenuScreen
import com.davidlukash.cakebaker.ui.screens.savescreen.SaveScreen
import com.davidlukash.cakebaker.ui.screens.upgradescreen.UpgradeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

const val transitionDuration = 500

@Composable
fun NormalScreenMessageManager(
    theme: Theme,
    popups: List<Popup>,
    trueDensity: Density,
    removePopup: (Int) -> Unit,
    lazyListState: LazyListState,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) { Popup(
            alignment = Alignment.BottomCenter,
        ) {
            MessageManager(
                theme = theme,
                popups = popups,
                trueDensity = trueDensity,
                removePopup = removePopup,
                lazyListState = lazyListState
            )
        }
        content()
    }
}

@Composable
fun Navigation(
    theme: Theme, uiState: UIState,
    pendingScreen: Screen?,
    saveFiles: List<SaveFile>,
    popups: List<Popup>,
    trueDensity: Density,
    removePopup: (Int) -> Unit,
    updateCurrentScreen: (Screen) -> Unit,
    navigateWithFade: (Screen) -> Unit,
    bake: () -> Unit, buyIngredient: (String) -> Unit,
    setAutoOvenEnabled: (Boolean) -> Unit,
    completeOrder: (Order) -> Unit,
    setCurrentCake: (Int) -> Unit,
    exportSave: (SaveFile) -> Unit,
    deleteSave: (SaveFile) -> Unit,
    loadSave: (SaveFile) -> Unit,
    importSave: () -> Unit,
    overwriteSave: (SaveFile) -> Unit,
    buyUpgrade: (Upgrade) -> Unit
) {
    val navController = rememberNavController()
    val lazyListState = rememberLazyListState()
    val backStack by navController.currentBackStackEntryAsState()
    val currentScreen by derivedStateOf {
        allScreensMap[backStack?.destination?.route]
    }

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
    val navGraph = remember(navController, uiState, currentScreen, popups, saveFiles) {
        navController.createGraph(startDestination = CakeScreen) {
            composable<IngredientScreen> {
                NormalScreenMessageManager(
                    theme = theme,
                    popups = popups,
                    trueDensity = trueDensity,
                    removePopup = removePopup,
                    lazyListState = lazyListState,
                    {
                        Background(theme) {
                            IngredientScreen(theme, uiState, navigateWithFade, buyIngredient)
                        }
                    }
                )
            }
            composable<CakeScreen> {
                NormalScreenMessageManager(
                    theme = theme,
                    popups = popups,
                    trueDensity = trueDensity,
                    removePopup = removePopup,
                    lazyListState = lazyListState,
                    {
                        Background(theme) {
                            CakeScreen(theme, navigateWithFade)
                        }
                    }
                )
            }
            composable<MenuScreen> {
                NormalScreenMessageManager(
                    theme = theme,
                    popups = popups,
                    trueDensity = trueDensity,
                    removePopup = removePopup,
                    lazyListState = lazyListState,
                    {
                        Background(theme) {
                            MenuScreen(theme, navigateWithFade)
                        }
                    }
                )
            }
            composable<KitchenScreen> {
                NormalScreenMessageManager(
                    theme = theme,
                    popups = popups,
                    trueDensity = trueDensity,
                    removePopup = removePopup,
                    lazyListState = lazyListState,
                    {
                        Background(theme) {
                            KitchenScreen(
                                theme,
                                uiState,
                                navigateWithFade,
                                bake,
                                setAutoOvenEnabled,
                                completeOrder,
                                setCurrentCake
                            )
                        }
                    }
                )
            }
            composable<UpgradeScreen> {
                NormalScreenMessageManager(
                    theme = theme,
                    popups = popups,
                    trueDensity = trueDensity,
                    removePopup = removePopup,
                    lazyListState = lazyListState,
                    {
                        Background(theme) {
                            UpgradeScreen(theme, uiState, navigateWithFade, buyUpgrade)
                        }
                    }
                )
            }
            composable<SaveScreen> {
                NormalScreenMessageManager(
                    theme = theme,
                    popups = popups,
                    trueDensity = trueDensity,
                    removePopup = removePopup,
                    lazyListState = lazyListState,
                    {
                        Background(theme) {
                            SaveScreen(theme, saveFiles, navigateWithFade, exportSave, deleteSave, loadSave, overwriteSave, importSave)
                        }
                    }
                )
            }
        }
    }
    NavHost(navController, navGraph, enterTransition = {
        fadeIn(
            animationSpec = tween(transitionDuration)
        )
    }, exitTransition = {
        fadeOut(
            animationSpec = tween(transitionDuration)
        )
    })
}

@Preview(
    widthDp = 1920,
    heightDp = 1080
)
@Composable
fun NavigationPreview() {
    val theme = getDefaultTheme()
    val uiState = Save.state
    var currentScreen by remember { mutableStateOf<Screen>(IngredientScreen) }
    Navigation(
        theme = theme,
        uiState = uiState,
        pendingScreen = currentScreen,
        saveFiles = listOf(),
        updateCurrentScreen = { currentScreen = it },
        navigateWithFade = { currentScreen = it },
        bake = {},
        buyIngredient = {},
        setAutoOvenEnabled = {},
        completeOrder = {},
        setCurrentCake = {},
        exportSave = {},
        deleteSave = {},
        loadSave = {},
        importSave = {},
        overwriteSave = {},
        buyUpgrade = {},
        popups = listOf(),
        trueDensity = LocalDensity.current,
        removePopup = {},
    )
}