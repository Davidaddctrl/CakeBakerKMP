package com.davidlukash.cakebaker.ui.screens.kitchenscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.Order
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.secondsToString
import com.davidlukash.cakebaker.toEngNotation
import com.davidlukash.cakebaker.ui.Container

import com.davidlukash.cakebaker.ui.ProgressBar
import com.davidlukash.cakebaker.ui.ResourceImage
import com.davidlukash.cakebaker.ui.SmallThemedButton
import com.davidlukash.cakebaker.ui.SwitchButton
import com.davidlukash.cakebaker.ui.TertiaryContainer
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal

@Composable
fun RowScope.OrdersPanel(theme: Theme, uiState: UIState, completeOrder: (Order) -> Unit) {
    val nextOrderRemainingTime = uiState.nextOrderRemainingTime
    val orders = uiState.orders

    Container(
        theme = theme,
        modifier = Modifier.weight(1f).fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().height(720.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Text(
                    "Orders",
                    style = theme.labelStyle,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
                Text(
                    orders.size.toString(),
                    style = theme.smallLabelStyle,
                )
            }

            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (orders.isEmpty()) {
                    nextOrderRemainingTime?.let {
                        Text(
                            "${secondsToString(it)} until next order",
                            style = theme.labelStyle,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (nextOrderRemainingTime == null)
                        Text(
                        "Bake a cake to get orders",
                        style = theme.labelStyle,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(
                        orders.size,
                        key = { orders[it].id }
                    ) { orderIndex ->
                        val order = orders[orderIndex]
                        OrderItem(theme, uiState, { completeOrder(order) }, order)
                    }
                }
            }
        }
    }
}
