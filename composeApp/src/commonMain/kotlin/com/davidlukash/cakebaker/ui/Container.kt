package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Container(theme: Theme, modifier: Modifier, content: @Composable () -> Unit) {
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

@Preview
@Composable
fun ContainerPreview() {
    val theme = getDefaultTheme()
    Container(
        theme, Modifier.size(400.dp)
    ) {
        Text(text = "Container Preview", style = theme.labelStyle)
    }
}