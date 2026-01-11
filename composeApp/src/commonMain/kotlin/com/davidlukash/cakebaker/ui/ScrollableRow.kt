package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import com.davidlukash.cakebaker.horizontalRowScroll
import kotlinx.coroutines.CoroutineScope

@Composable
fun ScrollableRow(
    horizontalArrangement: Arrangement.Horizontal,
    verticalAlignment: Alignment.Vertical,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable RowScope.(Boolean) -> Unit
) {
    var availableWidth by remember { mutableStateOf(0) }
    var measuredWidth by remember { mutableStateOf(0) }
    val shouldScroll = measuredWidth > availableWidth
    MeasureLayout(
        onMeasured = {
            measuredWidth = it.width
        }
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
        ) {
            content(true)
        }
    }

    Row(
        modifier = modifier.onGloballyPositioned {
            availableWidth = it.size.width
        }.then(
            if (shouldScroll)
                Modifier.horizontalScroll(scrollState).horizontalRowScroll(coroutineScope, scrollState)
            else Modifier
        ),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        content(shouldScroll)
    }
}