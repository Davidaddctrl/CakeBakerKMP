package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.item.Item
import com.davidlukash.cakebaker.data.item.ItemType
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.globalDecimalMode
import com.davidlukash.cakebaker.toEngNotation
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ItemAmountDisplay(
    item: Item,
    quantityChange: BigDecimal = BigDecimal.ZERO,
    modifier: Modifier = Modifier
) {
    val image = Theme.getImage(item.image)
    val localContentColor = if (LocalInspectionMode.current) Color.White else LocalContentColor.current
    val textMeasurer = rememberTextMeasurer()
    val layoutResult = textMeasurer.measure(
        text = "A\nA",
        style = Theme.Styles.verySmallBodyStyle
    )
    val heightPx = layoutResult.size.height
    val labelHeight = LocalDensity.current.run { heightPx.toDp() }
    CompositionLocalProvider(
        LocalContentColor provides localContentColor
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Column(
                modifier = Modifier.height(labelHeight),
                verticalArrangement = Arrangement.Top
            ) {
                val name = Theme.getString(item.name)
                Text(
                    name.replace(" ", "\n"),
                    textAlign = TextAlign.Center,
                    style = if (name.contains(" ")) Theme.Styles.verySmallBodyStyle else Theme.Styles.smallBodyStyle,
                )
            }
            Column(
                modifier = Modifier.height(96.dp),
                verticalArrangement = Arrangement.Center
            ) {
                ResourceImage(
                    image,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.height(if (item.id == "item.money") 48.dp else 96.dp)
                )
            }
            Text(
                Theme.getString("label.amount"),
                textAlign = TextAlign.Center,
                style = Theme.Styles.smallBodyStyle,
            )
            Text(
                toEngNotation(item.amount.add(quantityChange, globalDecimalMode)),
                color = if (quantityChange == BigDecimal.ZERO) Color.Unspecified else
                    if (quantityChange > BigDecimal.ZERO) Theme.SuccessColor else Theme.DangerColor,
                textAlign = TextAlign.Center,
                style = Theme.Styles.smallBodyStyle,
            )
            Text(
                (if (quantityChange != BigDecimal.ZERO) {
                    (if (quantityChange > BigDecimal.ZERO) "+" else "-") + toEngNotation(quantityChange.abs())
                } else "") + " ",
                color = if (quantityChange == BigDecimal.ZERO) Color.Unspecified else
                    if (quantityChange > BigDecimal.ZERO) Theme.SuccessColor else Theme.DangerColor,
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start),
                style = Theme.Styles.verySmallBodyStyle,
            )
        }
    }
}

const val backgroundColor = 0xFF0078FFL

@Preview(showBackground = true, backgroundColor = backgroundColor)
@Composable
fun ItemAmountDisplayPreview() {
    val item = Item(
        name = "item.butter.name",
        image = "image.butter",
        type = ItemType.CURRENCY,
        amount = 10000.toBigDecimal(),
    )
    ItemAmountDisplay(item = item)
}

@Preview(showBackground = true, backgroundColor = backgroundColor)
@Composable
fun ItemAmountDisplayPositiveQuantityPreview() {
    val item = Item(
        name = "item.butter.name",
        image = "image.butter",
        type = ItemType.CURRENCY,
        amount = 10000.toBigDecimal(),
    )
    ItemAmountDisplay(item = item, quantityChange = 100.toBigDecimal())
}

@Preview(showBackground = true, backgroundColor = backgroundColor)
@Composable
fun ItemAmountDisplayNegativeQuantityPreview() {
    val item = Item(
        name = "item.butter.name",
        image = "image.butter",
        type = ItemType.CURRENCY,
        amount = 10000.toBigDecimal(),
    )
    ItemAmountDisplay(item = item, quantityChange = (-100).toBigDecimal())
}