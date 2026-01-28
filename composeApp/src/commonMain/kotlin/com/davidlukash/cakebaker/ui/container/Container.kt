package com.davidlukash.cakebaker.ui.container

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.ProvideContainer
import com.davidlukash.cakebaker.data.theme.Theme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Container(modifier: Modifier, shape: Shape = RoundedCornerShape(16.dp), content: @Composable () -> Unit) {
    Surface(
        modifier = modifier,
        color = Theme.ContainerTheme.containerColor,
        shape = shape,
        border = BorderStroke(8.dp, Theme.ContainerTheme.borderColor),
    ) {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            ProvideContainer(Theme.ContainerTheme) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun ContainerPreview() {
    Container(
        Modifier.size(400.dp), content = {
            Text(text = "Container Preview", style = Theme.Styles.smallBodyStyle)
        }
    )
}