package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.Item
import com.davidlukash.cakebaker.data.ItemType
import com.davidlukash.cakebaker.toEngNotation
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.ionspin.kotlin.bignum.decimal.BigDecimal

@Composable
fun BuyableItemDisplay(item: Item) {
    val mainViewModel = LocalMainViewModel.current
    val themeViewModel = mainViewModel.themeViewModel
    val dataViewModel = mainViewModel.dataViewModel
    val theme by themeViewModel.theme.collectAsState()
    val money by dataViewModel.itemMoneyFlow.collectAsState(initial = Item("Money", type = ItemType.CURRENCY, amount = BigDecimal.ZERO))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        ResourceImage(
            theme.nameToImage(item.name),
            modifier = Modifier.height(128.dp)
        )
        Container(
            modifier = Modifier.defaultMinSize(minWidth = 208.dp)
        ) {
            Column {
                Text(
                    item.name,
                    style = theme.smallLabelStyle,
                )
                Text(
                    "$${toEngNotation(item.price ?: BigDecimal.ZERO)}",
                    style = theme.subtitleStyle
                )
            }
        }
        LargeThemedButton(
            onClick = {
                dataViewModel.buyIngredient(item.name)
            },
            enabled = money.amount >= (item.price ?: BigDecimal.ZERO),
            modifier = Modifier.width(180.dp)
        ) {
            Text(
                "Buy",
                style = theme.buttonTextStyle,
            )
        }
    }
}