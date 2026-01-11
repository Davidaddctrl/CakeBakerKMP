package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.Save
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.horizontalRowScroll
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ItemTopRow(theme: Theme, uiState: UIState) {
    val items = uiState.items
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ScrollableRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) { shouldScroll ->
            items.forEach { item ->
                key(item.name) {
                    ItemAmountDisplay(
                        theme = theme,
                        item = item,
                        modifier = if (shouldScroll) Modifier else Modifier.weight(1f)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalScrollBar(theme, scrollState, coroutineScope)
    }
}

@Preview(
    widthDp = 1500,
)
@Composable
fun ItemTopRowPreview() {
    val theme = getDefaultTheme()
    val uiState = Save.state
    ItemTopRow(theme = theme, uiState = uiState)
}