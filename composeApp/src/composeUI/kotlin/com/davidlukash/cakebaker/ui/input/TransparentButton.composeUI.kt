package com.davidlukash.cakebaker.ui.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
    content: @Composable (() -> Unit)
) {
    val contentColor = LocalContentColor.current
    OutlinedButton(
        onClick = {
            onClick()
        },
        enabled = enabled,
        shape = RectangleShape,
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