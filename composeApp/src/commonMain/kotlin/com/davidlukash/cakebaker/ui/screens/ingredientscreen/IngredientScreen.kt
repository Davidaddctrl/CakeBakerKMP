package com.davidlukash.cakebaker.ui.screens.ingredientscreen

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun IngredientScreen() {
    Scaffold(
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBar()
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        MainContent(innerPadding)
    }
}