package com.davidlukash.cakebaker.ui.container

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.davidlukash.cakebaker.data.theme.Theme

@Composable
fun Background(content: @Composable () -> Unit) {
    BaseContainer(
        modifier = Modifier,
        theme = Theme.BackgroundTheme,
        values = Theme.BackgroundValues,
        shapeOverrideFactory = null,
    ) {
        content()
    }
}