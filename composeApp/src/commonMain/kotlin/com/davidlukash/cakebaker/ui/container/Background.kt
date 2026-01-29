package com.davidlukash.cakebaker.ui.container

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.ProvideContainer
import com.davidlukash.cakebaker.data.theme.Theme

@Composable
fun Background(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(Theme.BackgroundTheme.containerColor).padding(16.dp)
    ) {
        ProvideContainer(Theme.BackgroundTheme) {
            content()
        }
    }
}