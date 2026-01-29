package com.davidlukash.cakebaker.ui.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.LocalDoDropShadow
import com.davidlukash.cakebaker.data.theme.Theme

import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
expect fun LargeThemedButton(
    onClick: () -> Unit,
    modifier: com.davidlukash.cakebaker.platformui.Modifier = com.davidlukash.cakebaker.platformui.Modifier,
    enabled: Boolean = true,
    onHoverChange: (Boolean) -> Unit = {},
    content: @Composable () -> Unit
)

@Composable
fun LargeThemedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onHoverChange: (Boolean) -> Unit = {},
    content: @Composable () -> Unit,
) {
    LargeThemedButton(
        onClick = onClick,
        modifier = com.davidlukash.cakebaker.platformui.Modifier.nativeComposeModifier(modifier),
        enabled = enabled,
        onHoverChange = onHoverChange,
        content = content
    )
}

@Preview(
    widthDp = 768
)
@Composable
fun LargeThemedButtonPreview() {
    LargeThemedButton(
        onClick = {},
        modifier = Modifier,
        content = {
            Text("Button Preview")
        },
    )
}