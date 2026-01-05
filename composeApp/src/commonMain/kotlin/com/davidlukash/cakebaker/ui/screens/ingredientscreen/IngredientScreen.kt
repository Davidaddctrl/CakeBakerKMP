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

@Composable
fun IngredientScreen(theme: Theme, uiState: UIState, buyIngredient: (String) -> Unit) {
    LaunchedEffect(uiState) {
        println("UIState IngredientScreen: ${uiState.getMoneyItem()}")
    }
    Scaffold(
        topBar = {
            TopBar(theme, uiState)
        },
        bottomBar = {
            BottomBar(theme)
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