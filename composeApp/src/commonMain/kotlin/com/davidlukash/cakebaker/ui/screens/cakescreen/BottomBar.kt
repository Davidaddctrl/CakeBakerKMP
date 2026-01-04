package com.davidlukash.cakebaker.ui.screens.cakescreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.davidlukash.cakebaker.VERSION

import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel

@Composable
fun BottomBar() {
    val mainViewModel = LocalMainViewModel.current
    val themeViewModel = mainViewModel.themeViewModel
    val theme by themeViewModel.theme.collectAsState()
    Text(
        "Kotlin $VERSION",
        color = Color.White,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        style = theme.subtitleStyle,
    )
}