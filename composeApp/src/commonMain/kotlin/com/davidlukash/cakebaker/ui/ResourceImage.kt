package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import cakebaker.composeapp.generated.resources.Res
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.davidlukash.cakebaker.data.ImageData
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ResourceImage(data: ImageData, contentDescription: String? = null, modifier: Modifier = Modifier) {
    key(data) {
        val bitmap = imageResource(data.resource)
        val placeholder = BitmapPainter(bitmap, filterQuality = data.filterQuality)
        AsyncImage(
            model = data.imagePath,
            contentDescription = contentDescription,
            modifier = modifier,
            placeholder = placeholder,
            error = placeholder,
            filterQuality = data.filterQuality
        )
    }
}

@Preview
@Composable
fun ResourceImagePreview() {
    val theme = getDefaultTheme()
    ResourceImage(theme.nameToImage("Oven"))
}