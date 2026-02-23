package com.davidlukash.cakebaker.ui.input

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.davidlukash.cakebaker.data.theme.LocalIsScaled
import com.davidlukash.cakebaker.data.theme.Theme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SmallThemedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isScaled: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    CompositionLocalProvider(
        LocalIsScaled provides isScaled,
    ) {
        BaseThemedButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
            values = Theme.SmallButtonValues,
            theme = Theme.ButtonTheme,
            content = content
        )
    }
}

@Preview
@Composable
fun SmallThemedButtonPreview() {
    SmallThemedButton(
        onClick = {},
        content = {
            Text("Small Button Preview")
        },
    )
}