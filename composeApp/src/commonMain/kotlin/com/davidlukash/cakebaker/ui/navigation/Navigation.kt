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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Popup
import androidx.navigation.compose.currentBackStackEntryAsState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.Background
import com.davidlukash.cakebaker.ui.MessageManager
import com.davidlukash.cakebaker.ui.screens.cakescreen.CakeScreen
import com.davidlukash.cakebaker.ui.screens.ingredientscreen.IngredientScreen
import com.davidlukash.cakebaker.ui.screens.kitchenscreen.KitchenScreen
import com.davidlukash.cakebaker.ui.screens.menuscreen.MenuScreen
import com.davidlukash.cakebaker.ui.screens.savescreen.SaveScreen
import com.davidlukash.cakebaker.ui.screens.upgradescreen.UpgradeScreen

const val transitionDuration = 750

@Composable
fun NormalScreenMessageManager(theme: Theme, lazyListState: LazyListState, content: @Composable () -> Unit) {
    val uiViewModel = LocalMainViewModel.current.uiViewModel
    val currentScreen by uiViewModel.currentScreen.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        if (currentScreen is FadeScreen) MessageManager(theme = theme, lazyListState = lazyListState)
        else Popup(
            alignment = Alignment.BottomCenter,
        ) {
            MessageManager(theme = theme, lazyListState = lazyListState)
        }
        content()
    }
}

@Composable
fun Navigation(theme: Theme) {
    val uiViewModel = LocalMainViewModel.current.uiViewModel
    val pendingScreen by uiViewModel.pendingScreen.collectAsState()
    val navController = rememberNavController()
    val lazyListState = rememberLazyListState()
    val backStack by navController.currentBackStackEntryAsState()
    val currentScreen by derivedStateOf {
        allScreensMap[backStack?.destination?.route]
    }

    LaunchedEffect(currentScreen) {
        currentScreen?.let {
            uiViewModel.updateCurrentScreen(it as Screen)
        }
    }

    LaunchedEffect(pendingScreen) {
        pendingScreen?.let {
            navController.navigate(it as Screen)
        }
    }
    val navGraph = remember(navController) {
        navController.createGraph(startDestination = SaveScreen) {
            composable<CakeScreen> {
                NormalScreenMessageManager(theme = theme, lazyListState = lazyListState) {
                    Background(theme) {
                        CakeScreen(theme)
                    }
                }
            }
            composable<FadeScreen> {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black),
                )
            }
            composable<MenuScreen> {
                NormalScreenMessageManager(theme = theme, lazyListState = lazyListState) {
                    Background(theme) {
                        MenuScreen(theme)
                    }
                }
            }
            composable<KitchenScreen> {
                NormalScreenMessageManager(theme = theme, lazyListState = lazyListState) {
                    Background(theme) {
                        KitchenScreen(theme)
                    }
                }
            }
            composable<IngredientScreen> {
                NormalScreenMessageManager(theme = theme, lazyListState = lazyListState) {
                    Background(theme) {
                        IngredientScreen(theme)
                    }
                }
            }
            composable<UpgradeScreen> {
                NormalScreenMessageManager(theme = theme, lazyListState = lazyListState) {
                    Background(theme) {
                        UpgradeScreen(theme)
                    }
                }
            }
            composable<SaveScreen> {
                NormalScreenMessageManager(theme = theme, lazyListState = lazyListState) {
                    Background(theme) {
                        SaveScreen(theme)
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