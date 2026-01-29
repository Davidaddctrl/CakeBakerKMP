package com.davidlukash.cakebaker.platformui.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.davidlukash.cakebaker.platformui.HorizontalArrangement
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.ModifierNode
import com.davidlukash.cakebaker.platformui.RowScope
import com.davidlukash.cakebaker.platformui.modifiers
import org.jetbrains.compose.web.dom.Div

@Composable
actual fun Row(
    modifier: Modifier,
    horizontalArrangement: HorizontalArrangement,
    verticalAlignment: Alignment.Vertical,
    content: @Composable (RowScope.() -> Unit)
) {
    Div(
        attrs = {
            modifiers(modifier)
            style {
                property("display", "flex")
                property("flex-direction", "row")
                when (horizontalArrangement) {
                    is HorizontalArrangement.SpacedBy -> {
                        property("gap", horizontalArrangement.space.value.toString() + "px")
                        property(
                            "justify-content", when (horizontalArrangement.alignment) {
                                Alignment.Top -> "flex-start"
                                Alignment.CenterVertically -> "center"
                                Alignment.Bottom -> "flex-end"
                                else -> "flex-start"
                            }
                        )
                    }

                    is HorizontalArrangement.SpaceBetween -> property("justify-content", "space-between")
                    is HorizontalArrangement.SpaceEvenly -> property("justify-content", "space-evenly")
                    is HorizontalArrangement.SpaceAround -> property("justify-content", "space-around")
                    is HorizontalArrangement.Start -> property("justify-content", "flex-start")
                    is HorizontalArrangement.Center -> property("justify-content", "center")
                    is HorizontalArrangement.End -> property("justify-content", "flex-end")
                }
                when (verticalAlignment) {
                    Alignment.Top -> property("align-items", "flex-start")
                    Alignment.CenterVertically -> property("align-items", "center")
                    Alignment.Bottom -> property("align-items", "flex-end")
                }
            }
        }
    ) {
        val scope = remember {
            object : RowScope {
                override fun Modifier.weight(weight: Float): Modifier {
                    return Modifier(this.nodes + ModifierNode.WeightNode(weight))
                }

                override fun Modifier.align(alignment: Alignment.Vertical): Modifier {
                    return Modifier(this.nodes + ModifierNode.VerticalAlignNode(alignment))
                }
            }
        }
        content(scope)
    }
}