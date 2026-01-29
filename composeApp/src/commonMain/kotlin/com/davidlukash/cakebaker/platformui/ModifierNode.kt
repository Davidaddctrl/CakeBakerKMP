package com.davidlukash.cakebaker.platformui

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
}