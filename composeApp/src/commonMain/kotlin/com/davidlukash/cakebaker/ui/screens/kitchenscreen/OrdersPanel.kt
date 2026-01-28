package com.davidlukash.cakebaker.ui.screens.kitchenscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.order.Order
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.secondsToString
import com.davidlukash.cakebaker.ui.container.PrimaryContainer

@Composable
fun RowScope.OrdersPanel(uiState: UIState, completeOrder: (Order) -> Unit, nextOrderRemainingTime: Double?, orders: List<Order>) {
    PrimaryContainer(
        modifier = Modifier.weight(1f).fillMaxWidth(),
        content = {
            Column(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    Text(
                        "Orders",
                        style = Theme.Styles.smallBodyStyle,
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )
                    Text(
                        orders.size.toString(),
                        style = Theme.Styles.verySmallBodyStyle,
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
                                style = Theme.Styles.smallBodyStyle,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        if (nextOrderRemainingTime == null)
                            Text(
                                "Bake a cake to get orders",
                                style = Theme.Styles.smallBodyStyle,
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
                            OrderItem(uiState, { completeOrder(order) }, order)
                        }
                    }
                }
            }
        },
    )
}
