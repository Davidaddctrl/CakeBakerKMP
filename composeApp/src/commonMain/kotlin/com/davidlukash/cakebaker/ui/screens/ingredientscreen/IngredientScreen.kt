package com.davidlukash.cakebaker.ui.screens.ingredientscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.navigation.Screen

@Composable
fun IngredientScreen(theme: Theme, uiState: UIState, navigateWithFade: (Screen) -> Unit, buyIngredient: (String) -> Unit) {
    Scaffold(
        topBar = {
            TopBar(theme, uiState)
        },
        bottomBar = {
            BottomBar(theme, navigateWithFade)
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
        ) {
            MainContent(theme, uiState, buyIngredient)
        }
    }
}