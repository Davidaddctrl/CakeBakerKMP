package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import cakebaker.composeapp.generated.resources.Res
import cakebaker.composeapp.generated.resources.missing
import coil3.compose.AsyncImage
import com.davidlukash.cakebaker.data.ImageData
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.logger
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ResourceImage(data: ImageData, contentScale: ContentScale, contentDescription: String? = null, modifier: Modifier = Modifier) {
    key(data) {
        val resourceBitmap = imageResource(data.resource)
        val placeholderBitmap = imageResource(Res.drawable.missing)
        val placeholder = BitmapPainter(placeholderBitmap, filterQuality = FilterQuality.None)
        val resource = BitmapPainter(resourceBitmap, filterQuality = data.filterQuality)
        if (data.imagePath != null)
            AsyncImage(
                model = data.imagePath,
                contentDescription = contentDescription,
                modifier = modifier,
                placeholder = placeholder,
                error = placeholder,
                filterQuality = data.filterQuality,
                onError = { error ->
                    (error.result.throwable as? Exception)?.let { exception ->
                        logger.logError(exception)
                    }
                },
                contentScale = contentScale
            )
        else
            Image(
                painter = resource,
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
    }
}

@Preview
@Composable
fun ResourceImagePreview() {
    ResourceImage(Theme.getImage("Oven"), contentScale = ContentScale.FillHeight,)
}