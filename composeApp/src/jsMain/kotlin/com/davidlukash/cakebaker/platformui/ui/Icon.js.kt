package com.davidlukash.cakebaker.platformui.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import cakebaker.composeapp.generated.resources.Res
import com.davidlukash.cakebaker.platformui.ImageResource
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.modifiers
import com.davidlukash.cakebaker.toCSS
import org.jetbrains.compose.web.dom.Div

@Composable
actual fun Icon(
    imageResource: ImageResource,
    contentDescription: String?,
    tint: Color,
    modifier: Modifier
) {
    Div(
        attrs = {
            modifiers(modifier)
            style {
                property("mask-image", "url(\"${Res.getUri("drawable/${imageResource.name}")}\")")
                property("mask-repeat", "no-repeat")
                property("mask-size", "contain")
                property("mask-position", "center")
                property(
                    "background-color",
                    tint.toCSS()
                )
            }
        }
    ) {}
}