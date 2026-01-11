package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Container(theme: Theme, modifier: Modifier, content: @Composable () -> Unit) {
    val containerTheme = theme.containerTheme
    Surface(
        modifier = modifier,
        color = containerTheme.containerColor,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(8.dp, containerTheme.borderColor),
    ) {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            CompositionLocalProvider(
                LocalContentColor provides containerTheme.contentColor,
            ) {
                content()
            }
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
        Text(text = "Container Preview", style = theme.scaledStyles.smallBodyStyle)
    }
}