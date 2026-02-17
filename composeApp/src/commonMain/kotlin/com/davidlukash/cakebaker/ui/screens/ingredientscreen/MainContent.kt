package com.davidlukash.cakebaker.ui.screens.ingredientscreen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.horizontalRowScroll
import com.davidlukash.cakebaker.ui.BuyableItemDisplay
import com.davidlukash.cakebaker.ui.input.HorizontalScrollBar
import com.ionspin.kotlin.bignum.decimal.BigDecimal

@Composable
fun BoxScope.MainContent(
    uiState: UIState,
    buyIngredient: (String) -> Unit,
    setQuantityChanges: (Map<String, BigDecimal>) -> Unit
) {
    val ingredients by derivedStateOf { uiState.getIngredients() }
    val money by derivedStateOf { uiState.getMoneyItem() }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.align(Alignment.Center)
    ) {
        Row(
            modifier = Modifier.horizontalScroll(scrollState)
                .horizontalRowScroll(coroutineScope, scrollState).align(Alignment.CenterHorizontally).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            ingredients.forEach { ingredient ->
                key(ingredient.id) {
                    BuyableItemDisplay(money, buyIngredient, ingredient) { isHovered ->
                        if (isHovered) setQuantityChanges(
                            mapOf(
                                ingredient.id to (ingredient.increment ?: BigDecimal.ZERO),
                                money.id to (ingredient.price?.negate() ?: BigDecimal.ZERO),
                            )
                        ) else setQuantityChanges(mapOf())
                    }
                }
            }
        }
        HorizontalScrollBar(scrollState, coroutineScope)
    }
}