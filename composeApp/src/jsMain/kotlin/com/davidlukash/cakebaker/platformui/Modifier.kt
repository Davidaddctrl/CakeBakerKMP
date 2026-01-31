package com.davidlukash.cakebaker.platformui

import androidx.compose.ui.Alignment
import com.davidlukash.cakebaker.toCSS
import com.davidlukash.cakebaker.toClass
import org.jetbrains.compose.web.attributes.AttrsScope
import org.w3c.dom.Element

fun <T : Element> AttrsScope<T>.modifiers(modifier: Modifier) {
    modifier.nodes.reversed().forEach { node ->
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

        if (node is ModifierNode.FillMaxWidthNode) {
            style {
                property("width", "calc(100% * ${node.fraction})")
            }
        }

        if (node is ModifierNode.FillMaxHeightNode) {
            style {
                property("height", "calc(100% * ${node.fraction})")
            }
        }

        if (node is ModifierNode.BackgroundNode) {
            style {
                property("border-radius", node.shapeRadius.value.toString() + "px")
                property("background-color", node.backgroundColor.toCSS())
            }
        }

        if (node is ModifierNode.BorderNode) {
            style {
                property("border-style", "solid")
                property("border-radius", node.borderRadius.value.toString() + "px")
                property("border-width", node.width.value.toString() + "px")
                property("border-color", node.color.toCSS())
            }
        }

        if (node is ModifierNode.ShadowNode) {
            style {
                property("border-radius", node.shapeRadius.value.toString() + "px")
                property("box-shadow", "0 0 ${node.elevation.value}px 0 black")
            }
        }

        if (node is ModifierNode.PaddingNode) {
            style {
                property("padding", "${node.top.value / 2}px ${node.right.value / 2}px ${node.bottom.value / 2}px ${node.left.value / 2}px")
            }
        }

        if (node is ModifierNode.AlignmentNode) {
            classes(node.alignment.toClass())
        }
    }
}