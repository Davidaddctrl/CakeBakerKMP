package com.davidlukash.cakebaker.platformui

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

sealed class ModifierNode {
    class SizeNode(
        val width: Dp? = null,
        val height: Dp? = null,
        val minWidth: Dp? = null,
        val maxWidth: Dp? = null,
        val minHeight: Dp? = null,
        val maxHeight: Dp? = null
    ) : ModifierNode()

    class WeightNode(
        val weight: Float,
        val modifier: Modifier? = null
    ) : ModifierNode()

    class HorizontalAlignNode(
        val alignment: Alignment.Horizontal,
        val modifier: Modifier? = null
    ) : ModifierNode()

    class VerticalAlignNode(
        val alignment: Alignment.Vertical,
        val modifier: Modifier? = null
    ) : ModifierNode()

    class AlignmentNode(
        val alignment: Alignment,
        val modifier: Modifier? = null
    ) : ModifierNode()

    class FillMaxWidthNode(val fraction: Float) : ModifierNode()

    class FillMaxHeightNode(val fraction: Float) : ModifierNode()

    class BackgroundNode(val backgroundColor: Color, val shapeRadius: Dp) : ModifierNode()

    class BorderNode(val color: Color, val width: Dp, val borderRadius: Dp) : ModifierNode()

    class ShadowNode(val elevation: Dp, val shapeRadius: Dp) : ModifierNode()

    class PaddingNode(val left: Dp, val right: Dp, val top: Dp, val bottom: Dp) : ModifierNode()

    class NativeComposeNode(val modifier: Modifier) : ModifierNode()
}