package com.davidlukash.cakebaker.ui.screens.cakescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.ResourceImage
import com.davidlukash.cakebaker.ui.navigation.MenuScreen
import com.davidlukash.cakebaker.ui.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun CakeScreen(navigateWithFade: (Screen) -> Unit) {
    LaunchedEffect(Unit) {
        delay(500)
        navigateWithFade(MenuScreen)
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBar()
        },
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            ResourceImage(
                data = Theme.getImage("image.chocolate_cake"),
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.fillMaxSize(0.75f),
            )
        }
    }
}