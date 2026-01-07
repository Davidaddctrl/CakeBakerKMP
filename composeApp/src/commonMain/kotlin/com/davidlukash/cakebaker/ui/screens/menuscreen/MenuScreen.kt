package com.davidlukash.cakebaker.ui.screens.menuscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.LargeThemedButton

import com.davidlukash.cakebaker.ui.navigation.KitchenScreen
import com.davidlukash.cakebaker.ui.navigation.OtherScreen
import com.davidlukash.cakebaker.ui.navigation.SaveScreen
import com.davidlukash.cakebaker.ui.navigation.Screen
import com.davidlukash.cakebaker.ui.navigation.SettingsScreen

@Composable
fun MenuScreen(theme: Theme, navigateWithFade: (Screen) -> Unit) {
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
                    navigateWithFade(KitchenScreen)
                },
                modifier = Modifier.width(480.dp)
            ) {
                Text("Play", style = theme.buttonTextStyle)
            }
            LargeThemedButton(
                theme = theme,
                onClick = {
                    navigateWithFade(SaveScreen)
                },
                modifier = Modifier.width(480.dp)
            ) {
                Text("Saves", style = theme.buttonTextStyle)
            }
            LargeThemedButton(
                theme = theme,
                onClick = {
                    navigateWithFade(OtherScreen)
                },
                modifier = Modifier.width(480.dp)
            ) {
                Text("Licenses", style = theme.buttonTextStyle)
            }
            LargeThemedButton(
                theme = theme,
                onClick = {
                    navigateWithFade(SettingsScreen)
                },
                modifier = Modifier.width(480.dp)
            ) {
                Text("Settings", style = theme.buttonTextStyle)
            }
        }
    }
}