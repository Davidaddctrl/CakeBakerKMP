package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.ui.input.HorizontalScrollBar
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ItemTopRow(uiState: UIState, quantityChanges: Map<String, BigDecimal> = mapOf()) {
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
            verticalAlignment = Alignment.Bottom,
            scrollState = scrollState,
            coroutineScope = coroutineScope
        ) {
            items.forEach { item ->
                key(item.name) {
                    ItemAmountDisplay(
                        item = item,
                        quantityChange = quantityChanges[item.name] ?: BigDecimal.ZERO,
                        modifier = Modifier.widthIn(min = 128.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalScrollBar(scrollState, coroutineScope)
    }
}

@Preview(
    widthDp = 1920,
)
@Composable
fun ItemTopRowPreview() {
    val uiState = Save.state.copy(
        items = Save.state.items.map {
            if (it.name == "Money") it.copy(amount = 500.toBigDecimal()) else it
        }
    )
    ItemTopRow(
        uiState = uiState,
        quantityChanges = mapOf("Butter" to 0.2.toBigDecimal(), "Money" to (-250).toBigDecimal())
    )
}