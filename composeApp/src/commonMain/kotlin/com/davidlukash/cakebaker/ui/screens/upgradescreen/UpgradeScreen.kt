package com.davidlukash.cakebaker.ui.screens.upgradescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.Save
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.Upgrade
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.ui.navigation.Screen
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview

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

@Preview(
    widthDp = 1920,
    heightDp = 1080
)
@Composable
fun UpgradeScreenPreview() {
    val theme = getDefaultTheme()
    val uiState = Save.state.copy(
        items = Save.state.items.map {
            if (it.name == "Vanilla Cake") it.copy(amount = 5.toBigDecimal()) else it
        },
    )
    Box(
        modifier = Modifier.fillMaxSize().background(theme.backgroundTheme.containerColor),
    ) {
        UpgradeScreen(
            theme,
            uiState,
            {},
            {}
        )
    }
}