package com.davidlukash.cakebaker.ui.screens.menuscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.ui.LargeThemedButton
import com.davidlukash.cakebaker.ui.LocalFontFamily
import com.davidlukash.cakebaker.ui.navigation.Screen
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel

@Composable
fun MenuScreen() {
    val mainViewModel = LocalMainViewModel.current
    val themeViewModel = mainViewModel.themeViewModel
    val navigationViewModel = mainViewModel.uiViewModel
    val theme by themeViewModel.theme.collectAsState()
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopBar()
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LargeThemedButton(
                onClick = {
                    navigationViewModel.navigateWithFade(Screen.Kitchen)
                },
                modifier = Modifier.width(480.dp)
            ) {
                Text("Play", style = theme.buttonTextStyle, fontFamily = LocalFontFamily.current)
            }
        }
    }
}