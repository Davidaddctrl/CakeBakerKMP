package com.davidlukash.cakebaker.ui.input

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
import com.davidlukash.cakebaker.data.theme.LocalDoDropShadow
import com.davidlukash.cakebaker.data.theme.Theme

import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LargeThemedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        interactionSource = interactionSource,
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        enabled = enabled,
        contentPadding = PaddingValues(16.dp),
        border = BorderStroke(
            width = 8.dp,
            color = if (enabled) Theme.ButtonTheme.borderColor else Theme.ButtonTheme.disabledBorderColor,
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Theme.ButtonTheme.containerColor,
            disabledContainerColor = Theme.ButtonTheme.disabledContainerColor,
            contentColor = Theme.ButtonTheme.contentColor,
            disabledContentColor = Theme.ButtonTheme.disabledContentColor,
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
            LocalDoDropShadow provides Theme.ButtonTheme.shouldDropShadow
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides Theme.Styles.buttonTextStyle.copy(textAlign = TextAlign.Center),
            ) {
                content()
            }
        }
    }
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