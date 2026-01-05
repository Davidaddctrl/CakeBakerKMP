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
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.ResourceImage
import com.davidlukash.cakebaker.ui.navigation.MenuScreen
import com.davidlukash.cakebaker.ui.navigation.Screen
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import kotlinx.coroutines.delay

@Composable
fun CakeScreen(theme: Theme, navigateWithFade: (Screen) -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        navigateWithFade(MenuScreen)
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopBar(theme)
        },
        bottomBar = {
            BottomBar(theme)
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