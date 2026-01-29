package com.davidlukash.cakebaker.platformui.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.davidlukash.cakebaker.platformui.BoxScope
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.ModifierNode
import com.davidlukash.cakebaker.platformui.modifiers

@Composable
actual fun Box(
    modifier: Modifier,
    content: @Composable (BoxScope.() -> Unit)
) {
    Box(
        modifier = modifiers(modifier),
    ) {
        val scope = remember {
            object : BoxScope {
                override fun Modifier.align(alignment: Alignment): Modifier =
                    combineWithNode(
                        ModifierNode.AlignmentNode(
                            alignment,
                            androidx.compose.ui.Modifier.align(alignment)
                        )
                    )
            }
        }
        scope.content()
    }
}