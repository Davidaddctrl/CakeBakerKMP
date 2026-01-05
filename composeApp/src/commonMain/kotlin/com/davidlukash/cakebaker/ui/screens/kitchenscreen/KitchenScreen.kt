package com.davidlukash.cakebaker.ui.screens.kitchenscreen

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.theme.Theme

@Composable
fun KitchenScreen(theme: Theme) {
    Scaffold(
        topBar = {
            TopBar(theme)
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        MainContent(theme, innerPadding)
    }
}