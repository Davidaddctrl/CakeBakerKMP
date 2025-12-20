package com.davidlukash.cakebaker.ui.screens.upgradescreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.ui.ItemAmountDisplay
import com.davidlukash.cakebaker.ui.LocalFontFamily
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import kotlinx.coroutines.flow.compose

@Composable
fun TopBar() {
    val mainViewModel = LocalMainViewModel.current
    val themeViewModel = mainViewModel.themeViewModel
    val dataViewModel = mainViewModel.dataViewModel
    val items by dataViewModel.allItemsFlow.collectAsState(initial = emptyList())
    val theme by themeViewModel.theme.collectAsState()
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            "Upgrade Shop",
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = theme.titleStyle,
            fontFamily = LocalFontFamily.current
        )
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth(),
        ) {
            items.forEach { item ->
                key(item.name) {
                    ItemAmountDisplay(
                        item = item,
                    )
                }
            }
        }
    }
}