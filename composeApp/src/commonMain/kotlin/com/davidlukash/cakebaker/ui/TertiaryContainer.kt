package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
fun TertiaryContainer(theme: Theme, modifier: Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier = modifier,
        color = theme.buttonTheme.containerColor,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(4.dp, theme.containerBorderColor)
    ) {
        Box(
            modifier = Modifier.padding(8.dp)
        ) {
            CompositionLocalProvider(
                LocalContentColor provides theme.buttonTheme.contentColor
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun TertiaryContainerPreview() {
    val theme = getDefaultTheme()
    TertiaryContainer(theme = theme, modifier = Modifier.fillMaxSize()) {
        Text("Tertiary Container", style = theme.buttonTextStyle)
    }
}