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
fun LargeSecondaryContainer(modifier: Modifier, content: @Composable () -> Unit) {
    BaseContainer(
        modifier = modifier,
        theme = Theme.SecondaryContainerTheme,
        shape = RoundedCornerShape(16.dp),
        borderWidth = 8.dp,
        padding = 16.dp,
        shadowElevation = 0.dp,
        content = content,
    )
}

@Preview
@Composable
fun LargeSecondaryContainerPreview() {
    LargeSecondaryContainer(modifier = Modifier.fillMaxSize(), {
        Text("Large Secondary Container", style = Theme.Styles.titleStyle)
    })
}