package com.davidlukash.cakebaker.platformui.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.davidlukash.cakebaker.platformui.ColumnScope
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.ModifierNode
import com.davidlukash.cakebaker.platformui.VerticalArrangement
import com.davidlukash.cakebaker.platformui.modifiers
import org.jetbrains.compose.web.dom.Div

@Composable
actual fun Column(
    modifier: Modifier,
    verticalArrangement: VerticalArrangement,
    horizontalAlignment: Alignment.Horizontal,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Div(
        attrs = {
            modifiers(modifier)
            style {
                property("display", "flex")
                property("flex-direction", "column")
                when (verticalArrangement) {
                    is VerticalArrangement.SpacedBy -> {
                        property("gap", verticalArrangement.space.value.toString() + "px")
                        property(
                            "justify-content", when (verticalArrangement.alignment) {
                                Alignment.Top -> "flex-start"
                                Alignment.CenterVertically -> "center"
                                Alignment.Bottom -> "flex-end"
                                else -> "flex-start"
                            }
                        )
                    }

                    is VerticalArrangement.SpaceBetween -> property("justify-content", "space-between")
                    is VerticalArrangement.SpaceEvenly -> property("justify-content", "space-evenly")
                    is VerticalArrangement.SpaceAround -> property("justify-content", "space-around")
                    is VerticalArrangement.Top -> property("justify-content", "flex-start")
                    is VerticalArrangement.Center -> property("justify-content", "center")
                    is VerticalArrangement.Bottom -> property("justify-content", "flex-end")
                }
                when (horizontalAlignment) {
                    Alignment.Start -> property("align-items", "flex-start")
                    Alignment.CenterHorizontally -> property("align-items", "center")
                    Alignment.End -> property("align-items", "flex-end")
                }
            }
        }
    ) {
        val scope = remember {
            object : ColumnScope {
                override fun Modifier.weight(weight: Float): Modifier {
                    return Modifier(this.nodes + ModifierNode.WeightNode(weight))
                }

                override fun Modifier.align(alignment: Alignment.Horizontal): Modifier {
                    return Modifier(this.nodes + ModifierNode.HorizontalAlignNode(alignment))
                }
            }
        }
        content(scope)
    }
}