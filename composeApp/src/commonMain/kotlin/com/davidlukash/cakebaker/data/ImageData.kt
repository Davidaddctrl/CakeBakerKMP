package com.davidlukash.cakebaker.data

import androidx.compose.ui.graphics.FilterQuality
import cakebaker.composeapp.generated.resources.Res
import cakebaker.composeapp.generated.resources.missing
import com.davidlukash.cakebaker.platformui.ImageResource

data class ImageData(
    val resource: ImageResource = ImageResource(Res.drawable.missing, "missing.png"),
    val imagePath: String? = null,
    val filterQuality: FilterQuality = FilterQuality.None,
)
