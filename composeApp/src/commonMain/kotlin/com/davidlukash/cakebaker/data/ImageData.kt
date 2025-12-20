package com.davidlukash.cakebaker.data

import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import cakebaker.composeapp.generated.resources.Res
import cakebaker.composeapp.generated.resources.missing
import org.jetbrains.compose.resources.DrawableResource

data class ImageData(
    val resource: DrawableResource = Res.drawable.missing,
    val imagePath: String? = null,
    val filterQuality: FilterQuality = FilterQuality.None,
)
