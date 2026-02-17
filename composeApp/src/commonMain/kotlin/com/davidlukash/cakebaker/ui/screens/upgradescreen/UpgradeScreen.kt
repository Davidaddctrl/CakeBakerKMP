package com.davidlukash.cakebaker.ui.screens.upgradescreen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.Upgrade
import com.davidlukash.cakebaker.ui.container.Background
import com.davidlukash.cakebaker.ui.navigation.Screen
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UpgradeScreen(uiState: UIState, navigateWithFade: (Screen) -> Unit, buyUpgrade: (Upgrade) -> Unit, currentPage: String,
                  setCurrentPage: (String) -> Unit) {
    Scaffold(
        topBar = {
            TopBar(uiState)
        },
        bottomBar = {
            BottomBar(uiState, navigateWithFade, currentPage) { setCurrentPage(it) }
        },
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
    ) { innerPadding ->
        MainContent(uiState, buyUpgrade, innerPadding, currentPage)
    }
}

@Preview(
    widthDp = 1920,
    heightDp = 1200
)
@Composable
fun UpgradeScreenPreview() {
    val uiState = Save.state.copy(
        items = Save.state.items.map {
            if (it.id == "item.vanilla_cake") it.copy(amount = 5.toBigDecimal()) else it
        },
    )
    var currentPage by remember { mutableStateOf("") }
    Background {
        UpgradeScreen(
            uiState,
            navigateWithFade = {},
            buyUpgrade = {},
            currentPage = currentPage
        ) { currentPage = it }
    }
}