package com.davidlukash.cakebaker.ui.screens.themescreen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.DEFAULT_THEME_REGISTRY_URL
import com.davidlukash.cakebaker.ui.container.Background
import com.davidlukash.cakebaker.ui.navigation.Screen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ThemeScreen(
    themes: List<String>,
    initialSelectedThemes: List<String>,
    listThemes: () -> Unit,
    navigateWithFade: (Screen) -> Unit,
    exportTheme: (String) -> Unit,
    deleteTheme: (String) -> Unit,
    applyThemes: (List<String>) -> Unit,
    importTheme: () -> Unit,
    importThemeFromURL: (String) -> Unit,
) {
    var dialogState by remember { mutableStateOf<ThemeDialogState>(ThemeDialogState.None) }
    val selectedThemes = remember { mutableStateListOf<String>() }
    var hasLoadedSelectedThemes by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        listThemes()
    }

    LaunchedEffect(initialSelectedThemes) {
        selectedThemes.clear()
        selectedThemes.addAll(initialSelectedThemes)
        hasLoadedSelectedThemes = true
    }

    LaunchedEffect(Unit) {
        snapshotFlow { selectedThemes.toList() }.collect { selectedThemes ->
            if (hasLoadedSelectedThemes) applyThemes(selectedThemes)
        }
    }

    when (val state = dialogState) {
        ThemeDialogState.None -> {}
        is ThemeDialogState.Delete -> {
            DeleteThemeDialog(
                themeName = state.theme,
                delete = {
                    deleteTheme(state.theme)
                    dialogState = ThemeDialogState.None
                },
                cancel = { dialogState = ThemeDialogState.None }
            )
        }

        is ThemeDialogState.ImportFromURL -> {
            ImportURLThemeDialog(
                import = {
                    importThemeFromURL(it)
                    dialogState = ThemeDialogState.None
                },
                cancel = { dialogState = ThemeDialogState.None },
            )
        }
    }
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBar(
                import = { importTheme() },
                importDefaultThemes = { importThemeFromURL(DEFAULT_THEME_REGISTRY_URL) },
                importThemeFromURL = { dialogState = ThemeDialogState.ImportFromURL },
                navigateWithFade = navigateWithFade
            )
        },
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
    ) { innerPadding ->
        MainContent(
            themes = themes,
            exportTheme = { exportTheme(it) },
            deleteTheme = { dialogState = ThemeDialogState.Delete(it) },
            innerPadding = innerPadding,
            selectedThemes = selectedThemes,
        )
    }
}

@Preview(
    widthDp = 1920,
    heightDp = 1200
)
@Composable
fun ThemeScreenPreview() {
    Background {
        ThemeScreen(
            themes = listOf(
                "realistic",
                "moreitemstheme",
                "horrortheme",
            ),
            initialSelectedThemes = listOf(
                "realistic",
            ),
            listThemes = {},
            navigateWithFade = {},
            exportTheme = {},
            deleteTheme = {},
            applyThemes = {},
            importTheme = {},
            importThemeFromURL = {}
        )
    }
}