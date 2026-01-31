package com.davidlukash.cakebaker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.graphics.painter.BitmapPainter
import coil3.compose.AsyncImage
import com.davidlukash.cakebaker.data.ImageData
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.modifiers
import org.jetbrains.compose.resources.imageResource

@Composable
actual fun ResourceImage(
    data: ImageData,
    contentDescription: String?,
    modifier: Modifier
) {
    key(data) {
        val bitmap = imageResource(data.resource.resource)
        val placeholder = BitmapPainter(bitmap, filterQuality = data.filterQuality)
        AsyncImage(
            model = data.imagePath,
            contentDescription = contentDescription,
            modifier = modifiers(modifier),
            placeholder = placeholder,
            error = placeholder,
            filterQuality = data.filterQuality
        )
    }
}