package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import kotlinx.coroutines.delay

@Composable
fun MessageManager(
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val viewModel = LocalMainViewModel.current
    val themeViewModel = viewModel.themeViewModel
    val uiViewModel = viewModel.uiViewModel
    val popups by uiViewModel.popups.collectAsState()
    val theme by themeViewModel.theme.collectAsState()
    val trueDensity by uiViewModel.trueDensity.collectAsState()

    LaunchedEffect(popups) {
        if (popups.isNotEmpty())
            lazyListState.scrollToItem(popups.size - 1)
    }
    CompositionLocalProvider(
        LocalDensity provides (trueDensity ?: LocalDensity.current),
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
            modifier = Modifier.width(320.dp).zIndex(2f).padding(16.dp),
            state = lazyListState
        ) {
            itemsIndexed(popups, key = { _, (_, id) -> id }) { index, popup ->
                Box {
                    SmallContainer(
                        modifier = Modifier.width(320.dp),
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CompositionLocalProvider(
                                LocalTextStyle provides theme.smallLabelStyle.copy(
                                    fontFamily = LocalFontFamily.current,
                                    textAlign = TextAlign.Center,
                                )
                            ) {
                                popup.content(popup.copy(index = index))
                            }
                            if (popup.shouldHaveDefaultButton) {
                                Spacer(modifier = Modifier.height(8.dp))
                                SmallThemedButton(
                                    onClick = {
                                        uiViewModel.removePopup(index)
                                    }
                                ) {
                                    Text(
                                        "Dismiss",
                                        style = theme.smallLabelStyle,
                                        fontFamily = LocalFontFamily.current,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}