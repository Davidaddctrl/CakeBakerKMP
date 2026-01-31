package com.davidlukash.cakebaker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import cakebaker.composeapp.generated.resources.Res
import com.davidlukash.cakebaker.data.ImageData
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.modifiers
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Picture
import org.jetbrains.compose.web.dom.Source

@Composable
actual fun ResourceImage(
    data: ImageData,
    contentDescription: String?,
    modifier: Modifier
) {
    key(data) {
        Img(
            src = data.imagePath ?: Res.getUri("drawable/${data.resource.name}"),
            attrs = {
                modifiers(modifier)
            }
        )
    }
}