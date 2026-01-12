package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme

import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LargeThemedButton(
    theme: Theme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val buttonTheme = theme.buttonTheme
    Button(
        interactionSource = interactionSource,
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        enabled = enabled,
        contentPadding = PaddingValues(16.dp),
        border = BorderStroke(
            width = 8.dp,
            color = if (enabled) buttonTheme.borderColor else buttonTheme.disabledBorderColor,
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonTheme.containerColor,
            disabledContainerColor = buttonTheme.disabledContainerColor,
            contentColor = buttonTheme.contentColor,
            disabledContentColor = buttonTheme.disabledContentColor,
        ),
        elevation = ButtonDefaults.buttonElevation(
            0.dp,
            0.dp,
            0.dp,
            0.dp,
            0.dp
        )
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides theme.scaledStyles.buttonTextStyle.copy(textAlign = TextAlign.Center),
        ) {
            content()
        }
    }
}

@Preview(
    widthDp = 768
)
@Composable
fun LargeThemedButtonPreview() {
    val theme = getDefaultTheme()
    LargeThemedButton(
        theme,
        onClick = {},
    ) {
        Text("Button Preview")
    }
}