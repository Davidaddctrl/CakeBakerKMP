package com.davidlukash.cakebaker.ui.container

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.ProvideContainer
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.ui.Box

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