package com.davidlukash.cakebaker.ui.container

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.LocalIsScaled
import com.davidlukash.cakebaker.data.theme.ProvideContainer
import com.davidlukash.cakebaker.data.theme.Theme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SmallContainer(
    modifier: Modifier = Modifier,
    shadowElevation: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalIsScaled provides false
    ) {
        Surface(
            modifier = modifier,
            color = Theme.ContainerTheme.containerColor,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(6.dp, Theme.ContainerTheme.borderColor),
            shadowElevation = shadowElevation,
        ) {
            Box(
                modifier = modifier.padding(16.dp)
            ) {
                ProvideContainer(Theme.ContainerTheme) {
                    content()
                }
            }
        }
    }
}

@Composable
@Preview
fun SmallContainerPreview() {
    SmallContainer(
        modifier = Modifier.size(400.dp),
        content = {
            Text(
                "Small Container Preview",
                style = Theme.Styles.largeBodyStyle,
            )
        },
    )
}