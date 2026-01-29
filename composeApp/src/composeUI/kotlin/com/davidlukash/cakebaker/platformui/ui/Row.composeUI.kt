package com.davidlukash.cakebaker.platformui.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.davidlukash.cakebaker.platformui.ColumnScope
import com.davidlukash.cakebaker.platformui.HorizontalArrangement
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.ModifierNode
import com.davidlukash.cakebaker.platformui.RowScope
import com.davidlukash.cakebaker.platformui.modifiers

@Composable
actual fun Row(
    modifier: Modifier,
    horizontalArrangement: HorizontalArrangement,
    verticalAlignment: Alignment.Vertical,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifiers(modifier),
        horizontalArrangement = horizontalArrangement.toCompose(),
        verticalAlignment = verticalAlignment,
    ) {
        val scope = remember {
            object : RowScope {
                override fun Modifier.weight(weight: Float): Modifier = combineWithNode(
                    ModifierNode.WeightNode(
                        weight,
                        androidx.compose.ui.Modifier.weight(weight)
                    )
                )


                override fun Modifier.align(alignment: Alignment.Vertical): Modifier = combineWithNode(
                    ModifierNode.VerticalAlignNode(
                        alignment,
                        androidx.compose.ui.Modifier.align(alignment)
                    )
                )
            }
        }
        scope.content()
    }
}