package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel

@Composable
fun Background(theme: Theme, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(theme.backgroundColor).padding(16.dp)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides Color.White,
        ) {
            content()
        }
    }
}