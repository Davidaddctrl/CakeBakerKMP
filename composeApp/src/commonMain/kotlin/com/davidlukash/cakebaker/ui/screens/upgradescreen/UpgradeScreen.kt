package com.davidlukash.cakebaker.ui.screens.upgradescreen

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.Upgrade
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.navigation.Screen

@Composable
fun UpgradeScreen(theme: Theme, uiState: UIState, navigateWithFade: (Screen) -> Unit, buyUpgrade: (Upgrade) -> Unit) {
    var currentPage by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopBar(theme, uiState)
        },
        bottomBar = {
            BottomBar(theme, uiState, navigateWithFade, currentPage) { currentPage = it }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        MainContent(theme, uiState, buyUpgrade, innerPadding, currentPage)
    }
}