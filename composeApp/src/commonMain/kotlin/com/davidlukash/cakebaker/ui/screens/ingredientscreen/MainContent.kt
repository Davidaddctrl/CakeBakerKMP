package com.davidlukash.cakebaker.ui.screens.ingredientscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.ui.BuyableItemDisplay
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel

@Composable
fun MainContent(innerPadding: PaddingValues) {
    val mainViewModel = LocalMainViewModel.current
    val dataViewModel = mainViewModel.dataViewModel
    val ingredients by dataViewModel.ingredientsFlow.collectAsState(initial = emptyList())
    Row(
        modifier = Modifier.padding(innerPadding).fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ingredients.forEach { ingredient ->
            key(ingredient.name) {
                BuyableItemDisplay(ingredient)
            }
        }
    }
}