package com.davidlukash.cakebaker.platformui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import io.ktor.util.Platform

fun modifiers(modifier: com.davidlukash.cakebaker.platformui.Modifier): Modifier {
    var composeModifier: Modifier = Modifier
    modifier.nodes.forEach { node ->
        if (node is ModifierNode.SizeNode) {
            node.width?.let {
                composeModifier = composeModifier.width(it)
            }
            node.height?.let {
                composeModifier = composeModifier.height(it)
            }
            node.minWidth?.let {
                composeModifier = composeModifier.sizeIn(minWidth = it)
            }
            node.maxWidth?.let {
                composeModifier = composeModifier.sizeIn(maxWidth = it)
            }
            node.minHeight?.let {
                composeModifier = composeModifier.sizeIn(minHeight = it)
            }
            node.maxHeight?.let {
                composeModifier = composeModifier.sizeIn(maxHeight = it)
            }
        }
        
        if (node is ModifierNode.WeightNode) {
            node.modifier?.let {
                composeModifier = composeModifier.then(it)
            }
        }
        
        if (node is ModifierNode.HorizontalAlignNode) {
            node.modifier?.let {
                composeModifier = composeModifier.then(it)
            }
        }
        
        if (node is ModifierNode.VerticalAlignNode) {
            node.modifier?.let {
                composeModifier = composeModifier.then(it)
            }
        }

        if (node is ModifierNode.AlignmentNode) {
            node.modifier?.let {
                composeModifier = composeModifier.then(it)
            }
        }

        if (node is ModifierNode.FillMaxWidthNode) {
            composeModifier = composeModifier.fillMaxWidth(node.fraction)
        }

        if (node is ModifierNode.FillMaxHeightNode) {
            composeModifier = composeModifier.fillMaxHeight(node.fraction)
        }

        if (node is ModifierNode.BackgroundNode) {
            composeModifier = composeModifier.background(node.backgroundColor, RoundedCornerShape(node.shapeRadius))
        }

        if (node is ModifierNode.BorderNode) {
            composeModifier = composeModifier.border(BorderStroke(node.width, node.color), RoundedCornerShape(node.borderRadius))
        }

        if (node is ModifierNode.ShadowNode) {
            composeModifier = composeModifier.shadow(node.elevation, RoundedCornerShape(node.shapeRadius))
        }

        if (node is ModifierNode.PaddingNode) {
            composeModifier = composeModifier.padding(node.left, node.top, node.right, node.bottom)
        }

        if (node is ModifierNode.NativeComposeNode) {
            composeModifier = composeModifier.then(node.modifier)
        }
    }
    return composeModifier
}