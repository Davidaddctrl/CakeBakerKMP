package com.davidlukash.cakebaker.ui.container

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import com.davidlukash.cakebaker.data.theme.ContainerTheme
import com.davidlukash.cakebaker.data.theme.ProvideContainer

@Composable
fun BaseContainer(
    modifier: Modifier,
    theme: ContainerTheme,
    borderRadius: Dp,
    borderWidth: Dp,
    padding: Dp,
    shadowElevation: Dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(theme.containerColorBrush, RoundedCornerShape(borderRadius))
            .border(BorderStroke(borderWidth, theme.borderColorBrush), RoundedCornerShape(borderRadius))
            .shadow(shadowElevation, RoundedCornerShape(borderRadius))
            .padding(padding)
    ) {
        ProvideContainer(theme) {
            content()
        }
    }
}