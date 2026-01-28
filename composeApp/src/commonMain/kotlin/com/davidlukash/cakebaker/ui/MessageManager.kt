package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.davidlukash.cakebaker.data.Popup
import com.davidlukash.cakebaker.data.theme.LocalIsScaled
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.container.SmallPrimaryContainer
import com.davidlukash.cakebaker.ui.input.SmallThemedButton
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun MessageManager(
    popups: List<Popup>,
    trueDensity: Density,
    removePopup: (Uuid) -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    LaunchedEffect(popups) {
        if (popups.isNotEmpty())
            lazyListState.scrollToItem(popups.size - 1)
    }
    CompositionLocalProvider(
        LocalDensity provides trueDensity,
        LocalIsScaled provides false
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
            modifier = Modifier.width(320.dp).zIndex(2f).padding(16.dp),
            state = lazyListState
        ) {
            items(popups, key = { it.uuid }) { popup ->
                Popup({ removePopup(popup.uuid) }, popup)
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
fun Popup(remove: () -> Unit, popup: Popup) {
    CompositionLocalProvider(
        LocalIsScaled provides false
    ) {
        Box {
            SmallPrimaryContainer(
                modifier = Modifier.width(320.dp),
                shadowElevation = 8.dp,
                {
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CompositionLocalProvider(
                            LocalTextStyle provides Theme.Styles.largeBodyStyle.copy(
                                textAlign = TextAlign.Center,
                            )
                        ) {
                            popup.content.invoke(
                                popup
                            )
                        }
                        if (popup.shouldHaveDefaultButton) {
                            Spacer(modifier = Modifier.height(8.dp))
                            SmallThemedButton(
                                onClick = {
                                    remove()
                                },
                                content = {
                                    Text(
                                        "Dismiss",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Preview(
    heightDp = 120,
)
@Composable
fun PopupPreview() {
    val popup = Popup(
        content = {
            Text("Popup Preview", modifier = Modifier.fillMaxWidth())
        },
    )
    Popup(remove = {}, popup)
}