package com.davidlukash.cakebaker.ui.screens.upgradescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.Upgrade
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.toEngNotation
import com.davidlukash.cakebaker.ui.input.LargeThemedButton

import com.davidlukash.cakebaker.ui.ResourceImage
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UpgradeDisplay(uiState: UIState, buyUpgrade: (Upgrade) -> Unit, upgrade: Upgrade) {
    val cakes = uiState.getCakes()
    val cake = cakes[upgrade.cakeTier]
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp).width(480.dp)
    ) {
        Text(
            upgrade.name,
            style = Theme.Styles.smallBodyStyle,
            textAlign = TextAlign.Center,
        )
        ResourceImage(
            Theme.getImage(upgrade.imageName),
            modifier = Modifier.height(192.dp)
        )
        Text(
            "Level ${toEngNotation(upgrade.level.toBigDecimal())}" +
                    if (upgrade.maxLevel != null) " / ${toEngNotation(upgrade.maxLevel.toBigDecimal())} " else "",
            style = Theme.Styles.smallBodyStyle,
            textAlign = TextAlign.Center,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(32.dp)
        ) {
            if (upgrade.maxLevel?.let { upgrade.level < it } ?: true)
                Text(
                    "${toEngNotation(upgrade.price.toBigDecimal())} ${cake?.name.toString()}",
                    style = Theme.Styles.smallBodyStyle,
                ) else
                Text(
                    "Max Level Reached",
                    style = Theme.Styles.smallBodyStyle,
                )
        }
        LargeThemedButton(
            onClick = {
                buyUpgrade(upgrade)
            },
            modifier = Modifier.width(180.dp),
            enabled = (cake?.amount
                ?: BigDecimal.ZERO) >= upgrade.price && (upgrade.maxLevel?.let { upgrade.level < it } ?: true),
            content = {
                Text(
                    "Buy"
                )
            }
        )
    }
}

@Preview
@Composable
fun UpgradeDisplayPreview() {
    val uiState = Save.state.copy(
        items = Save.state.items.map { it.copy(amount = 1000.toBigDecimal()) }
    )
    val upgrade = uiState.upgrades.first()
    UpgradeDisplay(
        uiState = uiState,
        buyUpgrade = {},
        upgrade = upgrade
    )
}