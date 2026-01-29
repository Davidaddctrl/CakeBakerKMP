package com.davidlukash.cakebaker.ui.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.ButtonTokens
import com.davidlukash.cakebaker.data.theme.LocalDoDropShadow
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.modifiers

@Composable
actual fun LargeThemedButton(
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    onHoverChange: (Boolean) -> Unit,
    content: @Composable (() -> Unit)
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    LaunchedEffect(isHovered) {
        onHoverChange(isHovered)
    }
    Button(
        interactionSource = interactionSource,
        onClick = onClick,
        modifier = modifiers(modifier),
        shape = RoundedCornerShape(ButtonTokens.largeBorderRadius),
        enabled = enabled,
        contentPadding = PaddingValues(ButtonTokens.largePadding),
        border = BorderStroke(
            width = ButtonTokens.largeBorderWidth,
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