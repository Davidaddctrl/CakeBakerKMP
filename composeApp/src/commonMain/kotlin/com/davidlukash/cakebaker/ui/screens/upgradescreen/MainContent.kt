package com.davidlukash.cakebaker.ui.screens.upgradescreen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.Upgrade
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.horizontalRowScroll
import com.davidlukash.cakebaker.ui.HorizontalScrollBar
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel

@Composable
fun MainContent(theme: Theme, uiState: UIState, buyUpgrade: (Upgrade) -> Unit, innerPadding: PaddingValues, currentPage: String) {
    val upgrades = uiState.upgrades
    val filteredUpgrades = upgrades.filter { it.pageName == currentPage }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.horizontalScroll(scrollState)
                .horizontalRowScroll(coroutineScope, scrollState).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        ) {
            filteredUpgrades.forEach { upgrade ->
                key(upgrade.name) {
                    UpgradeDisplay(theme, uiState, buyUpgrade, upgrade)
                }
            }
        }
        HorizontalScrollBar(theme, scrollState, coroutineScope)
    }
}