package com.davidlukash.cakebaker.ui.screens.ingredientscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.navigation.Screen
import com.ionspin.kotlin.bignum.decimal.BigDecimal

@Composable
fun IngredientScreen(
    theme: Theme,
    uiState: UIState,
    navigateWithFade: (Screen) -> Unit,
    buyIngredient: (String) -> Unit
) {
    var quantityChanges by remember { mutableStateOf(mapOf<String, BigDecimal>()) }
    Scaffold(
        topBar = {
            TopBar(theme, uiState, quantityChanges)
        },
        bottomBar = {
            BottomBar(theme, navigateWithFade)
        },
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
        ) {
            MainContent(theme, uiState, buyIngredient) { quantityChanges = it }
        }
    }
}