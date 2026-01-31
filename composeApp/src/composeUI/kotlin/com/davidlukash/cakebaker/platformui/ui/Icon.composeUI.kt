package com.davidlukash.cakebaker.platformui.ui

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.platformui.ImageResource
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.modifiers
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun Icon(
    imageResource: ImageResource,
    contentDescription: String?,
    tint: Color,
    modifier: Modifier
) {
    key(imageResource) {
        Icon(
            painter = painterResource(imageResource.resource),
            contentDescription = contentDescription,
            tint = tint,
            modifier = modifiers(modifier),
        )
    }
}