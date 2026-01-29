package com.davidlukash.cakebaker.platformui

import androidx.compose.ui.Alignment
import org.jetbrains.compose.web.attributes.AttrsScope
import org.w3c.dom.Element

fun <T : Element> AttrsScope<T>.modifiers(modifier: Modifier) {
    modifier.nodes.forEach { node ->
        if (node is ModifierNode.SizeNode) {
            style {
                node.width?.let {
                    property("width", it.value.toString() + "px")
                }
                node.height?.let {
                    property("height", it.value.toString() + "px")
                }
                node.minWidth?.let {
                    property("min-width", it.value.toString() + "px")
                }
                node.maxWidth?.let {
                    property("max-width", it.value.toString() + "px")
                }
                node.minHeight?.let {
                    property("min-height", it.value.toString() + "px")
                }
                node.maxHeight?.let {
                    property("max-height", it.value.toString() + "px")
                }
            }
        }
        if (node is ModifierNode.WeightNode) {
            style {
                property("flex", node.weight.toString())
            }
        }
        if (node is ModifierNode.HorizontalAlignNode) {
            style {
                property(
                    "align-self", when (node.alignment) {
                        Alignment.Start -> "flex-start"
                        Alignment.CenterHorizontally -> "center"
                        Alignment.End -> "flex-end"
                        else -> "flex-start"
                    }
                )
            }
        }
        if (node is ModifierNode.VerticalAlignNode) {
            style {
                property(
                    "align-self", when (node.alignment) {
                        Alignment.Top -> "flex-start"
                        Alignment.CenterVertically -> "center"
                        Alignment.Bottom -> "flex-end"
                        else -> "flex-start"
                    }
                )
            }
        }
    }
}