package com.davidlukash.cakebaker.platformui

import androidx.compose.ui.unit.Dp

open class Modifier(val nodes: List<ModifierNode>) {
    fun size(
        width: Dp? = null,
        height: Dp? = null,
        minWidth: Dp? = null,
        maxWidth: Dp? = null,
        minHeight: Dp? = null,
        maxHeight: Dp? = null
    ): Modifier =
        Modifier(this.nodes + ModifierNode.SizeNode(width, height, minWidth, maxWidth, minHeight, maxHeight))

    fun nativeComposeModifier(modifier: androidx.compose.ui.Modifier): Modifier =
        Modifier(this.nodes + ModifierNode.NativeComposeNode(modifier))

    companion object : Modifier(listOf())
}