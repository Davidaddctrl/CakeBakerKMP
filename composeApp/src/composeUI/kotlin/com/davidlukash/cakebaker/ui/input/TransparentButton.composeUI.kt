package com.davidlukash.cakebaker.ui.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.modifiers

@Composable
actual fun TransparentButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier,
    padding: Dp,
    shapeRadius: Dp,
    onPress: (Boolean) -> Unit,
    onHover: (Boolean) -> Unit,
    content: @Composable (() -> Unit)
) {
    val contentColor = LocalContentColor.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    LaunchedEffect(isPressed) { onPress(isPressed) }

    LaunchedEffect(isHovered) { onHover(isHovered) }

    OutlinedButton(
        onClick = {
            onClick()
        },
        interactionSource = interactionSource,
        enabled = enabled,
        shape = RoundedCornerShape(shapeRadius),
        modifier = modifiers(modifier),
        border = BorderStroke(0.dp, Color.Transparent),
        elevation = ButtonDefaults.buttonElevation(
            0.dp,
            0.dp,
            0.dp,
            0.dp,
            0.dp
        ),
        contentPadding = PaddingValues(padding),
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
        ) {
            content()
        }
    }
}