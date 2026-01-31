package com.davidlukash.cakebaker.platformui.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.FilterQuality
import cakebaker.composeapp.generated.resources.Res
import com.davidlukash.cakebaker.platformui.ImageResource
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.modifiers
import org.jetbrains.compose.web.dom.Img

@Composable
actual fun Image(
    imageResource: ImageResource,
    contentDescription: String?,
    modifier: Modifier,
    filterQuality: FilterQuality
) {
    Img(
        src = Res.getUri("drawable/${imageResource.name}"),
        attrs = {
            modifiers(modifier)
        }
    )
}