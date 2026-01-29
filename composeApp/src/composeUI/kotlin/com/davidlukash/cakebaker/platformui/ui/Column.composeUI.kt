package com.davidlukash.cakebaker.platformui.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.davidlukash.cakebaker.platformui.ColumnScope
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.ModifierNode
import com.davidlukash.cakebaker.platformui.VerticalArrangement
import com.davidlukash.cakebaker.platformui.modifiers

@Composable
actual fun Column(
    modifier: Modifier,
    verticalArrangement: VerticalArrangement,
    horizontalAlignment: Alignment.Horizontal,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Column(
        modifier = modifiers(modifier),
        verticalArrangement = verticalArrangement.toCompose(),
        horizontalAlignment = horizontalAlignment,
    ) {
        val scope = remember {
            object : ColumnScope {
                override fun Modifier.weight(weight: Float): Modifier {
                    return Modifier(
                        this.nodes + ModifierNode.WeightNode(
                            weight,
                            androidx.compose.ui.Modifier.weight(weight)
                        )
                    )
                }

                override fun Modifier.align(alignment: Alignment.Horizontal): Modifier {
                    return Modifier(
                        this.nodes + ModifierNode.HorizontalAlignNode(
                            alignment,
                            androidx.compose.ui.Modifier.align(alignment)
                        )
                    )
                }
            }
        }
        scope.content()
    }
}