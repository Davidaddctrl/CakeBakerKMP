package com.davidlukash.cakebaker.ui.screens.cakescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.ui.ResourceImage
import com.davidlukash.cakebaker.ui.navigation.Screen
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import kotlinx.coroutines.delay

@Composable
fun CakeScreen() {
    val mainViewModel = LocalMainViewModel.current
    val themeViewModel = mainViewModel.themeViewModel
    val navigationViewModel = mainViewModel.uiViewModel
    val theme by themeViewModel.theme.collectAsState()

    LaunchedEffect(Unit) {
        delay(2000)
        navigationViewModel.navigateWithFade(Screen.Menu)
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBar()
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            ResourceImage(
                data = theme.nameToImage("Chocolate Cake"),
                modifier = Modifier.fillMaxSize(0.75f),
            )
        }
    }
}