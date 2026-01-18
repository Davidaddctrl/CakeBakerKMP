package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.item.Item
import com.davidlukash.cakebaker.data.item.ItemType
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.toEngNotation
import com.davidlukash.cakebaker.ui.container.Container
import com.davidlukash.cakebaker.ui.input.LargeThemedButton
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BuyableItemDisplay(theme: Theme, money: Item, buyIngredient: (String) -> Unit, item: Item, onHoverChange: (Boolean) -> Unit = {}) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    LaunchedEffect(isHovered) {
        onHoverChange(isHovered)
    }
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
            theme = theme,
            modifier = Modifier.defaultMinSize(minWidth = 208.dp)
        ) {
            Column {
                Text(
                    item.name,
                    style = theme.scaledStyles.verySmallBodyStyle,
                )
                Text(
                    "$${toEngNotation(item.price ?: BigDecimal.ZERO)}",
                    style = theme.scaledStyles.mediumBodyStyle
                )
            }
        }
        LargeThemedButton(
            theme = theme,
            onClick = {
                buyIngredient(item.name)
            },
            enabled = money.amount >= (item.price ?: BigDecimal.ZERO),
            modifier = Modifier.width(180.dp),
            interactionSource = interactionSource,
        ) {
            Text(
                "Buy",
            )
        }
    }
}

@Preview
@Composable
fun BuyableItemDisplayPreview() {
    val theme = Theme.default
    val money = Item(
        name = "Money",
        type = ItemType.CURRENCY,
        amount = 2000.toBigDecimal(),
    )
    val item = Item(
        name = "Butter",
        type = ItemType.INGREDIENT,
        amount = BigDecimal.ZERO,
        price = 200.toBigDecimal()
    )
    BuyableItemDisplay(
        theme = theme,
        money = money,
        buyIngredient = { },
        item = item
    )
}