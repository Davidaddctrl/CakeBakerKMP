package com.davidlukash.cakebaker.ui.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun ImageButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val contentColor = LocalContentColor.current
    OutlinedButton(
        onClick = {
            onClick()
        },
        enabled = enabled,
        shape = RectangleShape,
        modifier = modifier,
        border = BorderStroke(0.dp, Color.Transparent),
        elevation = ButtonDefaults.buttonElevation(
            0.dp,
            0.dp,
            0.dp,
            0.dp,
            0.dp
        ),
        contentPadding = PaddingValues(8.dp),
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
        ) {
            content()
        }
    }
}