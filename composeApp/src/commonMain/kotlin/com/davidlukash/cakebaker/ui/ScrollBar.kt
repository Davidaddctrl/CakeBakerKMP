package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.horizontalRowScroll
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HorizontalScrollBar(theme: Theme, scrollState: ScrollState, coroutineScope: CoroutineScope) {
    val density = LocalDensity.current
    val viewportWidth = density.run { scrollState.viewportSize.toDp() }
    val maxValue = density.run { scrollState.maxValue.toDp() } + viewportWidth
    val value = density.run { scrollState.value.toDp() }
    val visibleFraction = viewportWidth / maxValue
    if (scrollState.canScrollForward || scrollState.canScrollBackward) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Surface(
                color = theme.buttonTheme.containerColor,
                modifier = Modifier.fillMaxWidth(visibleFraction).height(32.dp)
                    .absoluteOffset(x = value * visibleFraction)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            coroutineScope.launch {
                                scrollState.scrollBy(dragAmount / visibleFraction)
                            }
                        }
                    }.border(6.dp, theme.buttonTheme.borderColor)
            ) {

            }
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun HorizontalScrollBarPreview() {
    val theme = getDefaultTheme()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.width(256.dp).height(128.dp)
    ) {
        Row(
            modifier = Modifier.horizontalScroll(scrollState).horizontalRowScroll(coroutineScope, scrollState).weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(10) {
                Text("Item $it", style = theme.labelStyle)
            }
        }
        HorizontalScrollBar(theme = theme, scrollState = scrollState, coroutineScope = coroutineScope)
    }
}