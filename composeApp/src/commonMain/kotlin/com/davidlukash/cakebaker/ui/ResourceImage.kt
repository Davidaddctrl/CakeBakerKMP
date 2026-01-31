package com.davidlukash.cakebaker.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.davidlukash.cakebaker.data.ImageData
import com.davidlukash.cakebaker.data.theme.Theme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ResourceImage(data: ImageData, contentDescription: String? = null, modifier: Modifier = Modifier) {
    ResourceImage(
        data = data,
        contentDescription = contentDescription,
        modifier = com.davidlukash.cakebaker.platformui.Modifier.nativeComposeModifier(modifier),
    )
}

@Composable
expect fun ResourceImage(
    data: ImageData,
    contentDescription: String?,
    modifier: com.davidlukash.cakebaker.platformui.Modifier = com.davidlukash.cakebaker.platformui.Modifier
)

@Preview
@Composable
fun ResourceImagePreview() {
    ResourceImage(Theme.getImage("Oven"), modifier = Modifier)
}