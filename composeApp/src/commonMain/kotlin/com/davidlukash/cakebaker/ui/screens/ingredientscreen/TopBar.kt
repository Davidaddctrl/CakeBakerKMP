package com.davidlukash.cakebaker.ui.screens.ingredientscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.ItemTopRow
import com.davidlukash.cakebaker.ui.input.MenuButton
import com.ionspin.kotlin.bignum.decimal.BigDecimal

@Composable
fun TopBar(uiState: UIState, quantityChanges: Map<String, BigDecimal>) {
    MenuButton {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Ingredient Shop",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = Theme.Styles.titleStyle,
            )
            ItemTopRow(uiState, quantityChanges)
        }
    }
}