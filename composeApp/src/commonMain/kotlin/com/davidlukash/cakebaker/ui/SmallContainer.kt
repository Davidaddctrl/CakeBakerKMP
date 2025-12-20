package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel

@Composable
fun SmallContainer(modifier: Modifier, shadowElevation: Dp = 0.dp, content: @Composable () -> Unit) {
    val mainViewModel = LocalMainViewModel.current
    val themeViewModel = mainViewModel.themeViewModel
    val dataViewModel = mainViewModel.dataViewModel
    val theme by themeViewModel.theme.collectAsState()
    Surface(
        modifier = modifier,
        color = theme.accentColor,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(6.dp, theme.containerBorderColor),
        shadowElevation = shadowElevation,
    ) {
        Box(
            modifier = Modifier.padding(16.dp).fillMaxSize()
        ) {
            content()
        }
    }
}