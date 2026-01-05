package com.davidlukash.cakebaker.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Density
import androidx.compose.ui.window.Popup
import androidx.navigation.compose.currentBackStackEntryAsState
import com.davidlukash.cakebaker.data.Order
import com.davidlukash.cakebaker.data.Popup
import com.davidlukash.cakebaker.data.Save
import com.davidlukash.cakebaker.data.SaveFile
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.Upgrade
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
    currentScreen: Screen?,
    lazyListState: LazyListState,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        if (currentScreen is FadeScreen) MessageManager(
            theme = theme,
            popups = popups,
            trueDensity = trueDensity,
            removePopup = removePopup,
            lazyListState = lazyListState
        )
        else Popup(
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
    val navGraph = remember(navController, uiState, currentScreen, popups) {
        navController.createGraph(startDestination = SaveScreen) {
            composable<IngredientScreen> {
                NormalScreenMessageManager(
                    theme = theme,
                    popups = popups,
                    trueDensity = trueDensity,
                    removePopup = removePopup,
                    currentScreen = currentScreen,
                    lazyListState = lazyListState
                ) {
                    Background(theme) {
                        IngredientScreen(theme, uiState, navigateWithFade, buyIngredient)
                    }
                }
            }
            composable<CakeScreen> {
                NormalScreenMessageManager(
                    theme = theme,
                    popups = popups,
                    trueDensity = trueDensity,
                    removePopup = removePopup,
                    currentScreen = currentScreen,
                    lazyListState = lazyListState
                ) {
                    Background(theme) {
                        CakeScreen(theme, navigateWithFade)
                    }
                }
            }
            composable<FadeScreen> {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black),
                )
            }
            composable<MenuScreen> {
                NormalScreenMessageManager(
                    theme = theme,
                    popups = popups,
                    trueDensity = trueDensity,
                    removePopup = removePopup,
                    currentScreen = currentScreen,
                    lazyListState = lazyListState
                ) {
                    Background(theme) {
                        MenuScreen(theme, navigateWithFade)
                    }
                }
            }
            composable<KitchenScreen> {
                NormalScreenMessageManager(
                    theme = theme,
                    popups = popups,
                    trueDensity = trueDensity,
                    removePopup = removePopup,
                    currentScreen = currentScreen,
                    lazyListState = lazyListState
                ) {
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
            }
            composable<UpgradeScreen> {
                NormalScreenMessageManager(
                    theme = theme,
                    popups = popups,
                    trueDensity = trueDensity,
                    removePopup = removePopup,
                    currentScreen = currentScreen,
                    lazyListState = lazyListState
                ) {
                    Background(theme) {
                        UpgradeScreen(theme, uiState, navigateWithFade, buyUpgrade)
                    }
                }
            }
            composable<SaveScreen> {
                NormalScreenMessageManager(
                    theme = theme,
                    popups = popups,
                    trueDensity = trueDensity,
                    removePopup = removePopup,
                    currentScreen = currentScreen,
                    lazyListState = lazyListState
                ) {
                    Background(theme) {
                        SaveScreen(theme, navigateWithFade, exportSave, deleteSave, loadSave, overwriteSave)
                    }
                }
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
        overwriteSave = {},
        buyUpgrade = {},
        popups = listOf(),
        trueDensity = LocalDensity.current,
        removePopup = {},
    )
}