package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
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
fun Container(modifier: Modifier, content: @Composable () -> Unit) {
    val mainViewModel = LocalMainViewModel.current
    val themeViewModel = mainViewModel.themeViewModel
    val dataViewModel = mainViewModel.dataViewModel
    val theme by themeViewModel.theme.collectAsState()
    Surface(
        modifier = modifier,
        color = theme.accentColor,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(8.dp, theme.containerBorderColor)
    ) {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            content()
        }
    }
}