package com.davidlukash.cakebaker.ui.container

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.davidlukash.cakebaker.data.theme.ContainerTheme
import com.davidlukash.cakebaker.data.theme.ContainerValues
import com.davidlukash.cakebaker.data.theme.ProvideContainer

@Composable
fun BaseContainer(
    modifier: Modifier,
    theme: ContainerTheme,
    values: ContainerValues,
    shapeOverrideFactory: ((Dp) -> Shape)?,
    content: @Composable () -> Unit
) {
    val elevation = values.elevation
    val borderWidth = values.borderWidth
    val shape = shapeOverrideFactory?.invoke(values.borderRadius) ?: RoundedCornerShape(values.borderRadius)
    Box(
        modifier = modifier
            .clip(shape)
            .shadow(elevation, shape)
            .background(theme.containerColorBrushDescriptor.toBrush(), shape)
            .border(BorderStroke(borderWidth, theme.borderColorBrushDescriptor.toBrush()), shape)
            .padding(values.contentPadding)
    ) {
        ProvideContainer(theme) {
            content()
        }
    }
}