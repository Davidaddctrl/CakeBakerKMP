package com.davidlukash.cakebaker.ui.container

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LargePrimaryContainer(
    modifier: Modifier = Modifier,
    shapeOverrideFactory: ((Dp) -> Shape)? = null,
    content: @Composable () -> Unit
) {
    BaseContainer(
        modifier = modifier,
        theme = Theme.ContainerTheme,
        values = Theme.LargePrimaryContainerValues,
        shapeOverrideFactory = shapeOverrideFactory,
        content = content
    )
}

@Preview
@Composable
fun ContainerPreview() {
    LargePrimaryContainer(
        Modifier.size(400.dp), content = {
            Text(text = "LargePrimaryContainer Preview", style = Theme.Styles.smallBodyStyle)
        }
    )
}