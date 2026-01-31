package com.davidlukash.cakebaker.platformui.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.FilterQuality
import com.davidlukash.cakebaker.platformui.ImageResource
import com.davidlukash.cakebaker.platformui.Modifier

@Composable
expect fun Image(
    imageResource: ImageResource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    filterQuality: FilterQuality = FilterQuality.High,
)