package com.davidlukash.cakebaker.platformui.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.davidlukash.cakebaker.data.theme.LocalDoDropShadow
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.modifiers
import com.davidlukash.cakebaker.toCSS
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
actual fun Text(
    text: String,
    style: TextStyle,
    modifier: Modifier
) {
    val doDropShadow = LocalDoDropShadow.current
    Span(
        attrs = {
            modifiers(modifier)
            style {
                property("font-size", style.fontSize.value.toString() + "px")
                property("text-align", style.textAlign.toString().lowercase())
                property("font-family", "vcr-osd-mono, monoscape")
                if (doDropShadow) {
                    style.shadow?.let { shadow ->
                        property(
                            "text-shadow",
                            "${shadow.offset.x}px ${shadow.offset.y}px ${shadow.blurRadius}px ${shadow.color.toCSS()}"
                        )
                    }
                }
            }
        }
    ) {
        Text(text)
    }
}