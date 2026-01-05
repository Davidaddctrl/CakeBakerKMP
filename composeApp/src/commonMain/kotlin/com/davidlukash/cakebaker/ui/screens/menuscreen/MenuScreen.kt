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
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.LargeThemedButton

import com.davidlukash.cakebaker.ui.navigation.KitchenScreen
import com.davidlukash.cakebaker.ui.navigation.SaveScreen
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel

@Composable
fun MenuScreen(theme: Theme) {
    val mainViewModel = LocalMainViewModel.current
    val uiViewModel = mainViewModel.uiViewModel
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopBar(theme)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LargeThemedButton(
                theme = theme,
                onClick = {
                    uiViewModel.navigateWithFade(KitchenScreen)
                },
                modifier = Modifier.width(480.dp)
            ) {
                Text("Play", style = theme.buttonTextStyle)
            }
            LargeThemedButton(
                theme = theme,
                onClick = {
                    uiViewModel.navigateWithFade(SaveScreen)
                },
                modifier = Modifier.width(480.dp)
            ) {
                Text("Saves", style = theme.buttonTextStyle)
            }
        }
    }
}