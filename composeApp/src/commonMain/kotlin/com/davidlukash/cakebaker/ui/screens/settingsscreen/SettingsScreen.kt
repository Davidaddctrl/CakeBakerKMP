package com.davidlukash.cakebaker.ui.screens.settingsscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.ConsoleType
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.ui.Background
import com.davidlukash.cakebaker.ui.SwitchButton
import com.davidlukash.cakebaker.ui.navigation.Screen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SettingsScreen(theme: Theme, navigateWithFade: (Screen) -> Unit, setDebugConsole: (ConsoleType) -> Unit, debugConsole: ConsoleType) {
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopBar(theme)
        },
        bottomBar = {
            BottomBar(theme, navigateWithFade)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(top = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Debug Console Open",
                style = theme.labelStyle,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            SwitchButton(
                theme = theme,
                value = debugConsole != ConsoleType.NONE,
                onText = "Open",
                offText = "Closed",
                modifier = Modifier.width(320.dp)
            ) {
                setDebugConsole(
                    if (debugConsole == ConsoleType.NONE) ConsoleType.SIDEBAR else ConsoleType.NONE
                )
            }
        }
    }
}

@Preview(
    widthDp = 1920,
    heightDp = 1080
)
@Composable
fun SettingsScreenPreview() {
    val theme = getDefaultTheme()
    Background(theme) {
        SettingsScreen(
            theme = theme,
            navigateWithFade = {},
            setDebugConsole = {},
            debugConsole = ConsoleType.SIDEBAR
        )
    }
}