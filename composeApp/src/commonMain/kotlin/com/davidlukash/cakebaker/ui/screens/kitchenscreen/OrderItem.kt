package com.davidlukash.cakebaker.ui.screens.kitchenscreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.Order
import com.davidlukash.cakebaker.data.Save
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.secondsToString
import com.davidlukash.cakebaker.toEngNotation

import com.davidlukash.cakebaker.ui.ProgressBar
import com.davidlukash.cakebaker.ui.SmallThemedButton
import com.davidlukash.cakebaker.ui.TertiaryContainer
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OrderItem(theme: Theme, uiState: UIState, completeOrder: () -> Unit, order: Order) {
    val cakes = uiState.getCakes()
    val cake = cakes[order.cakeTier]
    TertiaryContainer(
        theme = theme,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Order for",
                    style = theme.labelStyle,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                cake?.let { cake ->
                    SmallThemedButton(
                        theme = theme,
                        onClick = {
                            completeOrder()
                        },
                        enabled = cake.amount >= order.amount
                    ) {
                        Text(
                            "Complete",
                            style = theme.smallLabelStyle,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }
            cake?.let { cake ->
                Text(
                    "${toEngNotation(order.amount.toBigDecimal())} ${cake.name}",
                    style = theme.labelStyle,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
            }
            if (cake == null)
                Text(
                    "Invalid Cake Tier",
                    style = theme.labelStyle,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

            Text(
                "Buying for",
                style = theme.labelStyle,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
            Text(
                "$${toEngNotation(order.salePrice)}",
                style = theme.smallTitleStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
            Text(
                "Remaining Time",
                style = theme.labelStyle,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
            Box(
                contentAlignment = Alignment.Center,
            ) {
                ProgressBar(
                    theme,
                    order.remainingTime / order.totalTime
                )
                Text(
                    "${secondsToString(order.remainingTime)} remaining",
                    style = theme.smallLabelStyle,
                )
            }
            Text(
                "Order ${order.id}",
                style = theme.smallLabelStyle,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
        }
    }
}

@Preview(
    widthDp = 512
)
@Composable
fun OrderItemPreview() {
    val theme = getDefaultTheme()
    val uiState = Save.state.copy(
        items = Save.state.items.map { it.copy(amount = 10.toBigDecimal()) },
    )
    val infiniteTransition = rememberInfiniteTransition()
    val remainingTime by infiniteTransition.animateFloat(
        30f, 0f, animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
        )
    )
    val order = Order(
        cakeTier = 1,
        amount = 1,
        salePrice = 2500.toBigDecimal(),
        remainingTime = remainingTime.toDouble(),
        totalTime = 30.0,
        id = 988756,
    )
    OrderItem(theme = theme, uiState = uiState, completeOrder = {}, order = order)
}