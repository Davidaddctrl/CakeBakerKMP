package com.davidlukash.cakebaker.platformui.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.graphics.FilterQuality
import com.davidlukash.cakebaker.platformui.ImageResource
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.modifiers
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun Image(
    imageResource: ImageResource,
    contentDescription: String?,
    modifier: Modifier,
    filterQuality: FilterQuality
) {
    key(imageResource) {
        Image(
            painterResource(imageResource.resource),
            contentDescription = contentDescription,
            modifier = modifiers(modifier),
        )
    }
}