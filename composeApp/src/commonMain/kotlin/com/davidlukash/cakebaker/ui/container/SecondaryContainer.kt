package com.davidlukash.cakebaker.ui.container

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.ProvideContainer
import com.davidlukash.cakebaker.data.theme.Theme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SecondaryContainer(modifier: Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier = modifier,
        color = Theme.SecondaryContainerTheme.containerColor,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(4.dp, Theme.SecondaryContainerTheme.borderColor)
    ) {
        Box(
            modifier = Modifier.padding(8.dp)
        ) {
            ProvideContainer(Theme.SecondaryContainerTheme) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun SecondaryContainerPreview() {
    SecondaryContainer(modifier = Modifier.fillMaxSize(), {
        Text("Secondary Container", style = Theme.Styles.titleStyle)
    })
}