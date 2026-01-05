package com.davidlukash.cakebaker.ui.screens.kitchenscreen

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.Order
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.navigation.Screen

@Composable
fun KitchenScreen(
    theme: Theme, uiState: UIState,
    navigateWithFade: (Screen) -> Unit,
    bake: () -> Unit,
    setAutoOvenEnabled: (Boolean) -> Unit,
    completeOrder: (Order) -> Unit,
    setCurrentCake: (Int) -> Unit

) {
    Scaffold(
        topBar = {
            TopBar(theme, uiState)
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        MainContent(theme, uiState, navigateWithFade, bake, setAutoOvenEnabled, completeOrder, setCurrentCake, innerPadding)
    }
}