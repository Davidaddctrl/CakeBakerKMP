package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.Save
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ItemTopRow(theme: Theme, uiState: UIState, quantityChanges: Map<String, BigDecimal> = mapOf()) {
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
                        modifier = if (shouldScroll) Modifier else Modifier.weight(1f),
                        quantityChange = quantityChanges[item.name] ?: BigDecimal.ZERO,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalScrollBar(theme, scrollState, coroutineScope)
    }
}

@Preview(
    widthDp = 1920,
)
@Composable
fun ItemTopRowPreview() {
    val theme = getDefaultTheme()
    val uiState = Save.state.copy(
        items = Save.state.items.map {
            if (it.name == "Money") it.copy(amount = 500.toBigDecimal()) else it
        }
    )
    ItemTopRow(theme = theme, uiState = uiState, quantityChanges = mapOf("Butter" to 0.2.toBigDecimal(), "Money" to (-250).toBigDecimal()))
}