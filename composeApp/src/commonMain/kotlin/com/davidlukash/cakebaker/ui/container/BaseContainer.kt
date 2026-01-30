package com.davidlukash.cakebaker.ui.container

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.davidlukash.cakebaker.data.theme.ContainerTheme
import com.davidlukash.cakebaker.data.theme.ProvideContainer
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.ui.Box

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
            .background(theme.containerColor, borderRadius)
            .border(theme.borderColor, borderWidth, borderRadius)
            .shadow(shadowElevation, borderRadius)
            .padding(padding)
    ) {
        ProvideContainer(theme) {
            content()
        }
    }
}