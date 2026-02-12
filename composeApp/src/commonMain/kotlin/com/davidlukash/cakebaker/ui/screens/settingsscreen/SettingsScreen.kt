package com.davidlukash.cakebaker.ui.screens.settingsscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
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
import com.davidlukash.cakebaker.ProvideLocalDensity
import com.davidlukash.cakebaker.data.ConsoleType
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.container.Background
import com.davidlukash.cakebaker.ui.input.SwitchButton
import com.davidlukash.cakebaker.ui.navigation.Screen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SettingsScreen(
    navigateWithFade: (Screen) -> Unit,
    setDebugConsole: (ConsoleType) -> Unit,
    debugConsole: ConsoleType
) {
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBar(navigateWithFade)
        },
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding).padding(top = 32.dp)
        ) {
            ProvideLocalDensity(false) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Debug Console Open",
                        style = Theme.Styles.largeBodyStyle,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.fillMaxWidth()
                    )
                    SwitchButton(
                        value = debugConsole != ConsoleType.NONE,
                        onText = "Open",
                        offText = "Closed",
                        modifier = Modifier.width(320.dp),
                        height = 36.dp,
                        borderWidth = 6.dp,
                        textStyle = Theme.Styles.mediumBodyStyle
                    ) {
                        setDebugConsole(
                            if (debugConsole == ConsoleType.NONE) ConsoleType.POPUP else ConsoleType.NONE
                        )
                    }
                }
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
    Background({
        SettingsScreen(
            navigateWithFade = {},
            setDebugConsole = {},
            debugConsole = ConsoleType.SIDEBAR
        )
    })
}