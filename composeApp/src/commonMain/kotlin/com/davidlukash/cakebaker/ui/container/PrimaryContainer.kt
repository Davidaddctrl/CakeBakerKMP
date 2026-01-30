package com.davidlukash.cakebaker.ui.container

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.platformui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PrimaryContainer(modifier: Modifier, content: @Composable () -> Unit) {
    BaseContainer(
        modifier = modifier,
        theme = Theme.ContainerTheme,
        borderRadius = 16.dp,
        borderWidth = 8.dp,
        padding = 16.dp,
        shadowElevation = 0.dp,
        content = content
    )
}

@Composable
fun PrimaryContainer(modifier: androidx.compose.ui.Modifier, content: @Composable () -> Unit) {
    PrimaryContainer(
        modifier = Modifier.nativeComposeModifier(modifier),
        content = content
    )
}

@Preview
@Composable
fun ContainerPreview() {
    PrimaryContainer(
        Modifier.size(400.dp), content = {
            Text(text = "PrimaryContainer Preview", style = Theme.Styles.smallBodyStyle)
        }
    )
}