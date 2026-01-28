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
fun SmallThemedButton(
    theme: Theme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val buttonTheme = theme.buttonTheme
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        enabled = enabled,
        contentPadding = PaddingValues(8.dp),
        border = BorderStroke(
            width = 4.dp,
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
            LocalDoDropShadow provides buttonTheme.shouldDropShadow
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides theme.unscaledStyles.buttonTextStyle.copy(
                    textAlign = TextAlign.Center,
                ),
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun SmallThemedButtonPreview() {
    val theme = Theme.default
    SmallThemedButton(
        theme = theme,
        onClick = {},
    ) {
        Text("Small Button Preview")
    }
}