package com.davidlukash.cakebaker.ui.screens.kitchenscreen

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun KitchenScreen() {
    Scaffold(
        topBar = {
            TopBar()
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        MainContent(innerPadding)
    }
}