package com.davidlukash.cakebaker.ui.input

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import com.davidlukash.cakebaker.data.theme.ButtonTokens
import com.davidlukash.cakebaker.data.theme.LocalDoDropShadow
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.lighten
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.modifiers
import com.davidlukash.cakebaker.toCSS
import org.jetbrains.compose.web.dom.Button

@Composable
actual fun LargeThemedButton(
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    onHoverChange: (Boolean) -> Unit,
    content: @Composable (() -> Unit)
) {
    val buttonTheme = Theme.ButtonTheme
    var isHovered by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }
    LaunchedEffect(enabled) {
        if (!enabled) {
            isPressed = false
            isHovered = false
        }
    }
    Button(
        attrs = {
            modifiers(modifier)
            if (!enabled) {
                attr("disabled", "true")
            }
            if (enabled) {
                onClick {
                    onClick()
                }
                onMouseOver {
                    isHovered = true
                    onHoverChange(true)
                }
                onMouseOut {
                    isHovered = false
                    onHoverChange(false)
                }
                onMouseDown {
                    isPressed = true
                }
                onMouseUp {
                    isPressed = false
                }
            }
            style {
                property(
                    "background-color",
                    when {
                        !enabled -> buttonTheme.disabledContainerColor
                        isPressed -> buttonTheme.containerColor.lighten(0.15f)
                        isHovered -> buttonTheme.containerColor.lighten(0.1f)
                        else -> buttonTheme.containerColor
                    }.toCSS()
                )
                property(
                    "color",
                    if (enabled) buttonTheme.contentColor.toCSS()
                    else buttonTheme.disabledContentColor.toCSS()
                )
                property("border-style", "solid")
                property("border-width", ButtonTokens.largeBorderWidth.value.toString() + "px")
                property(
                    "border-color",
                    if (enabled) buttonTheme.borderColor.toCSS()
                    else buttonTheme.disabledBorderColor.toCSS()
                )
                property("border-radius", ButtonTokens.largeBorderRadius.value.toString() + "px")
                property("padding", ButtonTokens.largePadding.value.toString() + "px")
            }
        },
    ) {
        CompositionLocalProvider(
            LocalDoDropShadow provides Theme.ButtonTheme.shouldDropShadow,
            LocalContentColor provides if (enabled) buttonTheme.contentColor else buttonTheme.disabledContentColor
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides Theme.Styles.buttonTextStyle.copy(textAlign = TextAlign.Center),
            ) {
                content()
            }
        }
    }
}