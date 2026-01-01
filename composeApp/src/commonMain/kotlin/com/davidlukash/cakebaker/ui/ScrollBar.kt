package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HorizontalScrollBar(scrollState: ScrollState, coroutineScope: CoroutineScope) {
    val mainViewModel = LocalMainViewModel.current
    val density = LocalDensity.current
    val viewportWidth = density.run { scrollState.viewportSize.toDp() }
    val themeViewModel = mainViewModel.themeViewModel
    val theme by themeViewModel.theme.collectAsState()
    if (scrollState.canScrollForward || scrollState.canScrollBackward) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Surface(
                color = theme.buttonTheme.containerColor,
                modifier = Modifier.width(viewportWidth - scrollState.maxValue.dp).height(32.dp)
                    .absoluteOffset(x = scrollState.value.dp).pointerInput(Unit) {
                        detectHorizontalDragGestures { change, dragAmount ->
                            coroutineScope.launch {
                                scrollState.scrollBy(dragAmount)
                            }
                        }
                    }.border(6.dp, theme.buttonTheme.borderColor)
            ) {

            }
        }
    }
}