package com.davidlukash.cakebaker.platformui.ui

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import com.davidlukash.cakebaker.platformui.ImageResource
import com.davidlukash.cakebaker.platformui.Modifier

@Composable
expect fun Icon(
    imageResource: ImageResource,
    contentDescription: String?,
    tint: Color = LocalContentColor.current,
    modifier: Modifier = Modifier,
)