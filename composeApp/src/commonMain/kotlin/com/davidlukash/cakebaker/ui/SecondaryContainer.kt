package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
fun SecondaryContainer(theme: Theme, modifier: Modifier, content: @Composable () -> Unit) {
    val secondaryContainerTheme = theme.secondaryContainerTheme
    Surface(
        modifier = modifier,
        color = secondaryContainerTheme.containerColor,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(4.dp, secondaryContainerTheme.borderColor)
    ) {
        Box(
            modifier = Modifier.padding(8.dp)
        ) {
            CompositionLocalProvider(
                LocalContentColor provides secondaryContainerTheme.contentColor,
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun SecondaryContainerPreview() {
    val theme = getDefaultTheme()
    SecondaryContainer(theme = theme, modifier = Modifier.fillMaxSize()) {
        Text("Secondary Container", style = theme.buttonTextStyle)
    }
}