package com.davidlukash.cakebaker.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.item.Item
import com.davidlukash.cakebaker.data.item.ItemType
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.ui.Column
import com.davidlukash.cakebaker.platformui.VerticalArrangement
import com.davidlukash.cakebaker.platformui.ui.Text
import com.davidlukash.cakebaker.toEngNotation
import com.davidlukash.cakebaker.ui.container.PrimaryContainer
import com.davidlukash.cakebaker.ui.input.LargeThemedButton
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun BuyableItemDisplay(money: Item, buyIngredient: (String) -> Unit, item: Item, onHoverChange: (Boolean) -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = VerticalArrangement.SpacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        ResourceImage(
            Theme.getImage(item.name),
            contentDescription = item.name,
            modifier = Modifier.height(128.dp)
        )
        PrimaryContainer(
            modifier = Modifier.minSize(minWidth = 208.dp),
            content = {
                Column {
                    Text(
                        item.name,
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
                buyIngredient(item.name)
            },
            modifier = Modifier.width(180.dp),
            enabled = money.amount >= (item.price ?: BigDecimal.ZERO),
            onHoverChange = onHoverChange,
            content = {
                Text("Buy")
            },
        )
    }
}

@Preview
@Composable
fun BuyableItemDisplayPreview() {
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
        money = money,
        buyIngredient = { },
        item = item
    )
}