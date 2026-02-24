package com.davidlukash.cakebaker.ui.container

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SmallSecondaryContainer(modifier: Modifier, content: @Composable () -> Unit) {
    BaseContainer(
        modifier = modifier,
        theme = Theme.SecondaryContainerTheme,
        values = Theme.SmallSecondaryContainerValues,
        shapeOverrideFactory = null,
        content = content,
    )
}

@Preview
@Composable
fun SecondaryContainerPreview() {
    SmallSecondaryContainer(modifier = Modifier.fillMaxSize(), {
        Text("Small Secondary Container", style = Theme.Styles.titleStyle)
    })
}