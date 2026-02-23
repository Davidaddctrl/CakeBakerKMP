package com.davidlukash.cakebaker.ui.input

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import com.davidlukash.cakebaker.data.theme.Theme

import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LargeThemedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    val contentPadding = PaddingValues(Theme.LargeButtonValues.contentPadding)
    val textMeasurer = rememberTextMeasurer()
    val smallBodyStyle = Theme.Styles.smallBodyStyle
    val buttonStyle = Theme.Styles.buttonTextStyle
    val density = LocalDensity.current

    val smallBodyLayoutResult = remember(smallBodyStyle, density) {
        textMeasurer.measure(
            text = "A\nA",
            style = smallBodyStyle
        )
    }
    val buttonStyleLayoutResult = remember(buttonStyle, density) {
        textMeasurer.measure(
            text = "A",
            style = buttonStyle
        )
    }
    val heightPx = maxOf(smallBodyLayoutResult.size.height, buttonStyleLayoutResult.size.height)
    val buttonHeight =
        density.run { heightPx.toDp() } + contentPadding.calculateTopPadding() + contentPadding.calculateBottomPadding()

    BaseThemedButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = buttonHeight),
        enabled = enabled,
        interactionSource = interactionSource,
        values = Theme.LargeButtonValues,
        theme = Theme.ButtonTheme,
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
        content = {
            Text("Button Preview")
        },
    )
}