package com.davidlukash.cakebaker.ui.input

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.davidlukash.cakebaker.data.theme.ButtonTokens
import com.davidlukash.cakebaker.data.theme.LocalDoDropShadow
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.lighten
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.modifiers
import com.davidlukash.cakebaker.toCSS
import org.jetbrains.compose.web.dom.Button

@Composable
actual fun TransparentButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier,
    padding: Dp,
    content: @Composable (() -> Unit)
) {
    val contentColor = LocalContentColor.current
    var isHovered by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }
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
                }
                onMouseOut {
                    isHovered = false
                }
                onMouseDown {
                    isPressed = true
                }
                onMouseUp {
                    isPressed = false
                }
            }
            style {
                property("background-color",
                    if (isPressed) "rgba(0, 0, 0, 0.1)"
                    else if (isHovered) "rgba(0, 0, 0, 0.05)"
                    else "rgba(0, 0, 0, 0)"
                )
                property("border", "none")
                property("padding", padding.value.toString() + "px")
            }
        },
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides Theme.Styles.buttonTextStyle.copy(textAlign = TextAlign.Center),
            ) {
                content()
            }
        }
    }
}
