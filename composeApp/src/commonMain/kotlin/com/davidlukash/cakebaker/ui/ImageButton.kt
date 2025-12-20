package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.ui.navigation.Screen

@Composable
fun ImageButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
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
        )
    ) {
        content()
    }
}