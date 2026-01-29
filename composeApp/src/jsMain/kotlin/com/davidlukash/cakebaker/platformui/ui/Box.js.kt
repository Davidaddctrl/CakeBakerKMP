package com.davidlukash.cakebaker.platformui.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.davidlukash.cakebaker.platformui.BoxScope
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.ModifierNode
import com.davidlukash.cakebaker.platformui.modifiers
import com.davidlukash.cakebaker.toClass
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div

@Composable
actual fun Box(
    modifier: Modifier,
    content: @Composable (BoxScope.() -> Unit)
) {
    Div(
        attrs = {
            modifiers(modifier)
            style {
                position(Position.Relative)
            }
        }
    ) {
        val scope = remember {
            object : BoxScope {
                override fun Modifier.align(alignment: Alignment): Modifier = combineWithNode(
                    ModifierNode.AlignmentNode(alignment)
                )
            }
        }
        scope.content()
    }
}