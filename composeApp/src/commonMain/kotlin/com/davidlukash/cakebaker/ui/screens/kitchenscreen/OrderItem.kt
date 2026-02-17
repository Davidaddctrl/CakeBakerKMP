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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.order.Order
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.secondsToString
import com.davidlukash.cakebaker.toEngNotation

import com.davidlukash.cakebaker.ui.ProgressBar
import com.davidlukash.cakebaker.ui.input.SmallThemedButton
import com.davidlukash.cakebaker.ui.container.SecondaryContainer
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OrderItem(uiState: UIState, completeOrder: () -> Unit, order: Order) {
    val cakes by remember { derivedStateOf { uiState.getCakes() } }
    val cake by remember { derivedStateOf { cakes[order.cakeTier] } }
    val progressAmount by remember { derivedStateOf { order.remainingTime / order.totalTime } }
    SecondaryContainer(
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
                    Theme.getString("label.order_for"),
                    style = Theme.Styles.smallBodyStyle,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                cake?.let { cake ->
                    SmallThemedButton(
                        onClick = {
                            completeOrder()
                        },
                        enabled = cake.amount >= order.amount,
                        isScaled = true,
                        content = {
                            Text(
                                Theme.getString("action.complete"),
                                style = Theme.Styles.mediumBodyStyle,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    )
                }
            }
            cake?.let { cake ->
                Text(
                    "${toEngNotation(order.amount.toBigDecimal())} ${Theme.getString(cake.name)}",
                    style = Theme.Styles.smallBodyStyle,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
            }
            if (cake == null)
                Text(
                    Theme.getString("label.invalid_cake_tier"),
                    style = Theme.Styles.smallBodyStyle,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

            Text(
                Theme.getString("label.buying_for"),
                style = Theme.Styles.smallBodyStyle,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
            Text(
                "$${toEngNotation(order.salePrice)}",
                style = Theme.Styles.largeBodyStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
            Text(
                Theme.getString("label.remaining_time"),
                style = Theme.Styles.smallBodyStyle,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
            Box(
                contentAlignment = Alignment.Center,
            ) {
                ProgressBar(
                    modifier = Modifier.width(320.dp),
                    amount = progressAmount,
                )
                Text(
                    Theme.getString("label.remaining").replace("{0}", secondsToString(order.remainingTime)),
                    style = Theme.Styles.verySmallBodyStyle,
                )
            }
            Text(
                "${Theme.getString("label.order")} ${order.id}",
                style = Theme.Styles.verySmallBodyStyle,
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
    val uiState = Save.state.copy(
        items = Save.state.items.map { it.copy(amount = 10.toBigDecimal()) },
    )
    val infiniteTransition = rememberInfiniteTransition()
    val remainingTime by infiniteTransition.animateFloat(
        300f, 0f, animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
        )
    )
    val order = Order(
        cakeTier = 1,
        amount = 1,
        salePrice = 2500.toBigDecimal(),
        remainingTime = remainingTime.toDouble(),
        totalTime = 300.0,
        id = 988756,
    )
    OrderItem(uiState = uiState, completeOrder = {}, order = order)
}