package com.davidlukash.cakebaker.ui.input

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.platformui.Modifier

@Composable
fun TransparentButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    padding: Dp = 8.dp,
    shapeRadius: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    TransparentButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.nativeComposeModifier(modifier),
        padding = padding,
        shapeRadius = shapeRadius,
        content = content
    )
}

@Composable
expect fun TransparentButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    padding: Dp = 8.dp,
    shapeRadius: Dp = 0.dp,
    content: @Composable () -> Unit
)