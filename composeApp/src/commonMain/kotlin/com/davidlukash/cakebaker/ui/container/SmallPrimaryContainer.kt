package com.davidlukash.cakebaker.ui.container

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.LocalIsScaled
import com.davidlukash.cakebaker.data.theme.Theme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SmallPrimaryContainer(
    modifier: Modifier = Modifier,
    shadowElevation: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalIsScaled provides false
    ) {
        BaseContainer(
            modifier = modifier,
            theme = Theme.ContainerTheme,
            shape = RoundedCornerShape(8.dp),
            borderWidth = 6.dp,
            padding = 16.dp,
            shadowElevation = shadowElevation,
            content = content
        )
    }
}

@Composable
@Preview
fun SmallContainerPreview() {
    SmallPrimaryContainer(
        modifier = Modifier.size(400.dp),
        content = {
            Text(
                "Small Primary Container Preview",
                style = Theme.Styles.largeBodyStyle,
            )
        },
    )
}