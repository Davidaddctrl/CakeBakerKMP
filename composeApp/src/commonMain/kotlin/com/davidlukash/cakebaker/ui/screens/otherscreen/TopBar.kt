package com.davidlukash.cakebaker.ui.screens.otherscreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme

import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel

@Composable
fun TopBar(theme: Theme) {
    Text(
        "Licenses",
        color = Color.White,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        style = theme.titleStyle,
    )
}