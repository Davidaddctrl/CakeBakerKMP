package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.Item
import com.davidlukash.cakebaker.data.ItemType
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.globalDecimalMode
import com.davidlukash.cakebaker.toEngNotation
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ItemAmountDisplay(
    theme: Theme,
    item: Item,
    quantityChange: BigDecimal = BigDecimal.ZERO,
    modifier: Modifier = Modifier
) {
    val image = theme.nameToImage(item.name)
    val localContentColor = if (LocalInspectionMode.current) Color.White else LocalContentColor.current
    CompositionLocalProvider(
        LocalContentColor provides localContentColor
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                item.name.replace(" ", "\n"),
                textAlign = TextAlign.Center,
                style = if (item.name.contains(" ")) theme.scaledStyles.verySmallBodyStyle else theme.scaledStyles.smallBodyStyle,
            )
            Column(
                modifier = Modifier.height(96.dp),
                verticalArrangement = Arrangement.Center
            ) {
                ResourceImage(
                    image,
                    modifier = Modifier.height(if (item.name == "Money") 48.dp else 96.dp)
                )
            }
            Text(
                "Amount",
                textAlign = TextAlign.Center,
                style = theme.scaledStyles.smallBodyStyle,
            )
            Text(
                toEngNotation(item.amount.add(quantityChange, globalDecimalMode)),
                color = if (quantityChange == BigDecimal.ZERO) Color.White else
                    if (quantityChange > BigDecimal.ZERO) theme.successColor else theme.dangerColor,
                textAlign = TextAlign.Center,
                style = theme.scaledStyles.smallBodyStyle,
            )
            Text(
                (if (quantityChange != BigDecimal.ZERO) {
                    (if (quantityChange > BigDecimal.ZERO) "+" else "-") + toEngNotation(quantityChange.abs())
                } else "") + " ",
                color = if (quantityChange == BigDecimal.ZERO) Color.White else
                    if (quantityChange > BigDecimal.ZERO) theme.successColor else theme.dangerColor,
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start),
                style = theme.scaledStyles.verySmallBodyStyle,
            )
        }
    }
}

const val backgroundColor = 0xFF0078FFL

@Preview(showBackground = true, backgroundColor = backgroundColor)
@Composable
fun ItemAmountDisplayPreview() {
    val theme = Theme.default
    val item = Item(
        name = "Butter",
        type = ItemType.CURRENCY,
        amount = 10000.toBigDecimal(),
    )
    ItemAmountDisplay(theme = theme, item = item)
}

@Preview(showBackground = true, backgroundColor = backgroundColor)
@Composable
fun ItemAmountDisplayPositiveQuantityPreview() {
    val theme = Theme.default
    val item = Item(
        name = "Butter",
        type = ItemType.CURRENCY,
        amount = 10000.toBigDecimal(),
    )
    ItemAmountDisplay(theme = theme, item = item, quantityChange = 100.toBigDecimal())
}

@Preview(showBackground = true, backgroundColor = backgroundColor)
@Composable
fun ItemAmountDisplayNegativeQuantityPreview() {
    val theme = Theme.default
    val item = Item(
        name = "Butter",
        type = ItemType.CURRENCY,
        amount = 10000.toBigDecimal(),
    )
    ItemAmountDisplay(theme = theme, item = item, quantityChange = (-100).toBigDecimal())
}