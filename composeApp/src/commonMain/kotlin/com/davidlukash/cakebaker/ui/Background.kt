package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme

@Composable
fun Background(theme: Theme, content: @Composable () -> Unit) {
    val backgroundTheme = theme.backgroundTheme
    Box(
        modifier = Modifier.fillMaxSize().background(backgroundTheme.containerColor).padding(16.dp)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides backgroundTheme.contentColor,
        ) {
            content()
        }
    }
}