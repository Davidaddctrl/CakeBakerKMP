package com.davidlukash.cakebaker.ui.screens.ingredientscreen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.horizontalRowScroll
import com.davidlukash.cakebaker.ui.BuyableItemDisplay
import com.davidlukash.cakebaker.ui.HorizontalScrollBar
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel

@Composable
fun BoxScope.MainContent(theme: Theme, uiState: UIState, buyIngredient: (String) -> Unit) {
    val ingredients = uiState.getIngredients()
    val money = uiState.getMoneyItem()
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
                key(ingredient.name) {
                    BuyableItemDisplay(theme, money, buyIngredient, ingredient)
                }
            }
        }
        HorizontalScrollBar(theme, scrollState, coroutineScope)
    }
}