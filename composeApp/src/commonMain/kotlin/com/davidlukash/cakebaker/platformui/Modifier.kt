package com.davidlukash.cakebaker.platformui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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

    fun width(width: Dp) = size(width = width)

    fun height(height: Dp) = size(height = height)

    fun nativeComposeModifier(modifier: androidx.compose.ui.Modifier): Modifier =
        combineWithNode(ModifierNode.NativeComposeNode(modifier))

    fun fillMaxWidth(fraction: Float = 1f) = combineWithNode(ModifierNode.FillMaxWidthNode(fraction))

    fun fillMaxHeight(fraction: Float = 1f) = combineWithNode(ModifierNode.FillMaxHeightNode(fraction))

    fun fillMaxSize(fraction: Float = 1f) = fillMaxHeight(fraction).fillMaxHeight(fraction)

    fun background(color: Color, shapeRadius: Dp = 0.dp) = combineWithNode(ModifierNode.BackgroundNode(color, shapeRadius))

    fun border(color: Color, width: Dp, borderRadius: Dp = 0.dp) = combineWithNode(ModifierNode.BorderNode(color, width, borderRadius))

    fun shadow(elevation: Dp, shapeRadius: Dp = 0.dp) = combineWithNode(ModifierNode.ShadowNode(elevation, shapeRadius))

    fun padding(left: Dp = 0.dp, right: Dp = 0.dp, top: Dp = 0.dp, bottom: Dp = 0.dp) = combineWithNode(ModifierNode.PaddingNode(left, right, top, bottom))

    fun padding(all: Dp) = padding(all, all, all, all)

    fun padding(horizontal: Dp, vertical: Dp) = padding(horizontal, horizontal, vertical, vertical)

    companion object : Modifier(listOf())
}