package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.horizontalRowScroll
import com.davidlukash.cakebaker.platformui.HorizontalArrangement
import com.davidlukash.cakebaker.ui.input.HorizontalScrollBar
import com.davidlukash.cakebaker.usingHTML
import kotlinx.coroutines.CoroutineScope

@Composable
fun ScrollableRow(
    horizontalArrangement: Arrangement.Horizontal,
    verticalAlignment: Alignment.Vertical,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier.horizontalScroll(scrollState).horizontalRowScroll(coroutineScope, scrollState),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        content()
    }
}

@Composable
fun CrossScrollableRow(
    horizontalArrangement: HorizontalArrangement = HorizontalArrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    scrollBarSpacing: Dp = 16.dp,
    modifier: com.davidlukash.cakebaker.platformui.Modifier = com.davidlukash.cakebaker.platformui.Modifier,
    content: @Composable com.davidlukash.cakebaker.platformui.RowScope.() -> Unit
) {
    if (!usingHTML) {
        val scrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()
        Column {
            com.davidlukash.cakebaker.platformui.ui.Row(
                horizontalArrangement = horizontalArrangement,
                verticalAlignment = verticalAlignment,
                modifier = modifier.nativeComposeModifier(
                    Modifier.horizontalScroll(scrollState).horizontalRowScroll(coroutineScope, scrollState)
                )
            ) {
                content()
            }
            Spacer(modifier = Modifier.height(scrollBarSpacing))
            HorizontalScrollBar(
                scrollState = scrollState,
                coroutineScope = coroutineScope,
            )
        }
    } else {
        com.davidlukash.cakebaker.platformui.ui.Row(
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            modifier = modifier.css("overflow", "scroll")
        ) {
            content()
        }
    }
}