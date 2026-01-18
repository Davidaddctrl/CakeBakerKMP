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
fun Background(theme: Theme, content: @Composable () -> Unit) {
    val backgroundTheme = theme.backgroundTheme
    Box(
        modifier = Modifier.fillMaxSize().background(backgroundTheme.containerColor).padding(16.dp)
    ) {
        ProvideContainer(theme.backgroundTheme) {
            content()
        }
    }
}