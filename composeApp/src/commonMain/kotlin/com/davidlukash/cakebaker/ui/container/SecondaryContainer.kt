package com.davidlukash.cakebaker.ui.container

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SecondaryContainer(modifier: Modifier, content: @Composable () -> Unit) {
    BaseContainer(
        modifier = modifier,
        theme = Theme.SecondaryContainerTheme,
        borderRadius = 8.dp,
        borderWidth = 4.dp,
        padding = 8.dp,
        shadowElevation = 0.dp,
        content = content,
    )
}

@Preview
@Composable
fun SecondaryContainerPreview() {
    SecondaryContainer(modifier = Modifier.fillMaxSize(), {
        Text("Secondary Container", style = Theme.Styles.titleStyle)
    })
}