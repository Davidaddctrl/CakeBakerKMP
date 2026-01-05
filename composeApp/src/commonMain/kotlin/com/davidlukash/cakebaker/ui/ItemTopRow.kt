package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.horizontalRowScroll
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel

@Composable
fun ItemTopRow(theme: Theme, uiState: UIState) {
    val items = uiState.items
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.horizontalScroll(scrollState)
                .horizontalRowScroll(coroutineScope, scrollState).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        ) {
            items.forEach { item ->
                key(item.name) {
                    ItemAmountDisplay(
                        theme = theme,
                        item = item,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalScrollBar(theme, scrollState, coroutineScope)
    }
}