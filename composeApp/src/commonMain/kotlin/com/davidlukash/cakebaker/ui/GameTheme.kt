package com.davidlukash.cakebaker.ui

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import org.jetbrains.compose.resources.Font

@Composable
fun GameTheme(content: @Composable () -> Unit) {
    val themeViewModel = LocalMainViewModel.current.themeViewModel
    val theme by themeViewModel.theme.collectAsState()
    val family = FontFamily(Font(theme.font))
    CompositionLocalProvider(
        LocalFontFamily provides family
    ) {
        content()
    }
}

val LocalFontFamily = compositionLocalOf<FontFamily> { FontFamily.Default }