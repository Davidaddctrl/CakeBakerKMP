package com.davidlukash.cakebaker.platformui

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
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
        
        if (node is ModifierNode.NativeComposeNode) {
            composeModifier = composeModifier.then(node.modifier)
        }
    }
    return composeModifier
}