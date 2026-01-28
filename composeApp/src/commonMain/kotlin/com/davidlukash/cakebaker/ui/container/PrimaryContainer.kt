package com.davidlukash.cakebaker.ui.container

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PrimaryContainer(modifier: Modifier, shape: Shape = RoundedCornerShape(16.dp), content: @Composable () -> Unit) {
    BaseContainer(
        modifier = modifier,
        theme = Theme.ContainerTheme,
        shape = shape,
        borderWidth = 8.dp,
        padding = 16.dp,
        shadowElevation = 0.dp,
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