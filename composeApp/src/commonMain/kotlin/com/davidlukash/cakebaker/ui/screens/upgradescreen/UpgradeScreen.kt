package com.davidlukash.cakebaker.ui.screens.upgradescreen

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@Composable
fun UpgradeScreen() {
    var currentPage by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBar(currentPage) { currentPage = it }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        MainContent(innerPadding, currentPage)
    }
}