package com.davidlukash.cakebaker.ui.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.davidlukash.cakebaker.data.theme.ButtonElevation
import com.davidlukash.cakebaker.data.theme.ButtonTheme
import com.davidlukash.cakebaker.data.theme.ButtonValues
import com.davidlukash.cakebaker.data.theme.LocalDoDropShadow
import com.davidlukash.cakebaker.data.theme.Theme

@Composable
fun BaseThemedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    interactionSource: MutableInteractionSource,
    values: ButtonValues,
    theme: ButtonTheme,
    content: @Composable RowScope.() -> Unit,
) {
    val containerColorBrush =
        (if (enabled) theme.containerColorBrushDescriptor else theme.disabledContainerColorBrushDescriptor).toBrush()
    val contentColorBrushDescriptor =
        if (enabled) theme.contentColorBrushDescriptor else theme.disabledContentColorBrushDescriptor
    val contentColorBrush = contentColorBrushDescriptor.toBrush()
    val contentColor = contentColorBrushDescriptor.toColor()
    val buttonElevation = ButtonElevation(
        defaultElevation = values.defaultElevation,
        pressedElevation = values.pressedElevation,
        focusedElevation = values.focusedElevation,
        hoveredElevation = values.hoveredElevation,
        disabledElevation = values.disabledElevation,
    )
    val shadowElevation by buttonElevation.animateElevation(enabled, interactionSource)
    val shape = RoundedCornerShape(values.borderRadius)
    val borderStroke = BorderStroke(
        width = values.borderWidth,
        brush = (if (enabled) theme.borderColorBrushDescriptor else theme.disabledBorderColorBrushDescriptor).toBrush(),
    )
    Row(
        modifier = modifier
            .semantics { role = Role.Button }
            .shadow(shadowElevation, shape)
            .clip(shape)
            .background(containerColorBrush, shape)
            .border(borderStroke, shape)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = onClick
            )
            .padding(values.contentPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(
            LocalDoDropShadow provides theme.shouldDropShadow
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides
                        Theme.Styles.buttonTextStyle.copy(textAlign = TextAlign.Center, brush = contentColorBrush, fontWeight = if (theme.contentBold) FontWeight.Bold else  Theme.Styles.buttonTextStyle.fontWeight),
                LocalContentColor provides contentColor
            ) {
                content()
            }
        }
    }
}