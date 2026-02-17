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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.item.Item
import com.davidlukash.cakebaker.data.item.ItemType
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.toEngNotation
import com.davidlukash.cakebaker.ui.container.PrimaryContainer
import com.davidlukash.cakebaker.ui.input.LargeThemedButton
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BuyableItemDisplay(money: Item, buyIngredient: (String) -> Unit, item: Item, onHoverChange: (Boolean) -> Unit = {}) {
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
            Theme.getImage(item.image),
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.height(128.dp)
        )
        PrimaryContainer(
            modifier = Modifier.defaultMinSize(minWidth = 208.dp),
            content = {
                Column {
                    Text(
                        Theme.getString(item.name),
                        style = Theme.Styles.verySmallBodyStyle,
                    )
                    Text(
                        "$${toEngNotation(item.price ?: BigDecimal.ZERO)}",
                        style = Theme.Styles.mediumBodyStyle
                    )
                }
            }
        )
        LargeThemedButton(
            onClick = {
                buyIngredient(item.id)
            },
            modifier = Modifier.width(180.dp),
            enabled = money.amount >= (item.price ?: BigDecimal.ZERO),
            interactionSource = interactionSource,
            {
                Text(
                    Theme.getString("action.buy"),
                )
            },
        )
    }
}

@Preview
@Composable
fun BuyableItemDisplayPreview() {
    val money = Item(
        name = "item.money.name",
        id = "item.money",
        image = "image.money",
        type = ItemType.CURRENCY,
        amount = 2000.toBigDecimal(),
    )
    val item = Item(
        name = "item.butter.name",
        id = "item.butter",
        image = "image.butter",
        type = ItemType.INGREDIENT,
        amount = BigDecimal.ZERO,
        price = 200.toBigDecimal()
    )
    BuyableItemDisplay(
        money = money,
        buyIngredient = { },
        item = item
    )
}