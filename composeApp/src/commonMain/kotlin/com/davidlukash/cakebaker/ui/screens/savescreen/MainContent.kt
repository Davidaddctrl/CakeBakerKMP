package com.davidlukash.cakebaker.ui.screens.savescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.Save
import com.davidlukash.cakebaker.data.SaveFile
import com.davidlukash.cakebaker.ui.Container
import com.davidlukash.cakebaker.ui.LargeThemedButton
import com.davidlukash.cakebaker.ui.LocalFontFamily
import com.davidlukash.cakebaker.ui.navigation.KitchenScreen
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel

@Composable
fun MainContent(innerPadding: PaddingValues) {
    val mainViewModel = LocalMainViewModel.current
    val themeViewModel = mainViewModel.themeViewModel
    val uiViewModel = mainViewModel.uiViewModel
    val theme by themeViewModel.theme.collectAsState()
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize().padding(innerPadding).padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        columns = GridCells.Adaptive(760.dp)
    ) {
        item {
            SaveItem(SaveFile("Default", Save.default, true))
        }
        items(100) { index ->
            SaveItem(SaveFile("Save $index", Save.default))
        }
    }
}