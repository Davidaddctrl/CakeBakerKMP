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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import com.davidlukash.cakebaker.data.Popup
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MessageManager(
    theme: Theme,
    popups: List<Popup>,
    trueDensity: Density,
    removePopup: (Int) -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
) {
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
                Popup(theme, { removePopup(index) }, index, popup)
            }
        }
    }
}

@Composable
fun Popup(theme: Theme, remove: (Int) -> Unit, index: Int, popup: Popup) {
    Box {
        SmallContainer(
            theme = theme,
            modifier = Modifier.width(320.dp),
            shadowElevation = 8.dp
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                CompositionLocalProvider(
                    LocalTextStyle provides theme.smallLabelStyle.copy(
                        textAlign = TextAlign.Center,
                    )
                ) {
                    popup.content.invoke(
                        popup to theme
                    )
                }
                if (popup.shouldHaveDefaultButton) {
                    Spacer(modifier = Modifier.height(8.dp))
                    SmallThemedButton(
                        theme = theme,
                        onClick = {
                            remove(index)
                        }
                    ) {
                        Text(
                            "Dismiss",
                            style = theme.smallLabelStyle,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    heightDp = 120,
)
@Composable
fun PopupPreview() {
    val theme = getDefaultTheme()
    val popup = Popup(
        content = {
            Text("Popup Preview", modifier = Modifier.fillMaxWidth())
        },
        id = 0
    )
    Popup(theme = theme, remove = {}, -1, popup)
}