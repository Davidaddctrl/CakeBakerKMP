package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SmallContainer(
    theme: Theme,
    modifier: Modifier,
    shadowElevation: Dp = 0.dp,
    content: @Composable () -> Unit
) {
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

@Composable
@Preview
fun SmallContainerPreview() {
    val theme = getDefaultTheme()
    SmallContainer(
        theme = theme,
        modifier = Modifier.size(800.dp)
    ) {
        Text(
            "Small Container Preview",
            style = theme.labelStyle,
        )
    }
}