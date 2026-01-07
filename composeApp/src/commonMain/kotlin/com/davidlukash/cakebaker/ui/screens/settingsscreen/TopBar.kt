package com.davidlukash.cakebaker.ui.screens.settingsscreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.davidlukash.cakebaker.data.theme.Theme

@Composable
fun TopBar(theme: Theme) {
    Text(
        "Cake Baker",
        color = Color.White,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        style = theme.titleStyle,
    )
}