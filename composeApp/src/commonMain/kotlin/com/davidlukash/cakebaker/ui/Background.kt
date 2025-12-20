package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel

@Composable
fun Background(content: @Composable () -> Unit) {
    val mainViewModel = LocalMainViewModel.current
    val theme by mainViewModel.themeViewModel.theme.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize().background(theme.backgroundColor).padding(16.dp)
    ) {
        content()
    }
}