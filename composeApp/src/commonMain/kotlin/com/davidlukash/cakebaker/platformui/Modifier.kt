package com.davidlukash.cakebaker.platformui

import androidx.compose.ui.unit.Dp

open class Modifier(val nodes: List<ModifierNode>) {
    fun combineWithNode(node: ModifierNode): Modifier = Modifier(this.nodes + node)

    fun then(modifier: Modifier): Modifier = Modifier(this.nodes + modifier.nodes)

    fun size(
        width: Dp? = null,
        height: Dp? = null,
        minWidth: Dp? = null,
        maxWidth: Dp? = null,
        minHeight: Dp? = null,
        maxHeight: Dp? = null
    ): Modifier =
        combineWithNode(ModifierNode.SizeNode(width, height, minWidth, maxWidth, minHeight, maxHeight))

    fun nativeComposeModifier(modifier: androidx.compose.ui.Modifier): Modifier =
        combineWithNode(ModifierNode.NativeComposeNode(modifier))

    fun fillMaxWidth(fraction: Float = 1f) = combineWithNode(ModifierNode.FillMaxWidthNode(fraction))

    fun fillMaxHeight(fraction: Float = 1f) = combineWithNode(ModifierNode.FillMaxHeightNode(fraction))

    fun fillMaxSize(fraction: Float = 1f) = fillMaxHeight(fraction).fillMaxHeight(fraction)

    companion object : Modifier(listOf())
}