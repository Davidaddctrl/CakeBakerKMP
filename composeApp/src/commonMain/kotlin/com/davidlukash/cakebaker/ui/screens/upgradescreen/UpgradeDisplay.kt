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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.Save
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.Upgrade
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.toEngNotation
import com.davidlukash.cakebaker.ui.LargeThemedButton

import com.davidlukash.cakebaker.ui.ResourceImage
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UpgradeDisplay(theme: Theme, uiState: UIState, buyUpgrade: (Upgrade) -> Unit, upgrade: Upgrade) {
    val cakes = uiState.getCakes()
    val cake = cakes[upgrade.cakeTier]
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp).width(480.dp)
    ) {
        Text(
            upgrade.name,
            style = theme.scaledStyles.smallBodyStyle,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
        ResourceImage(
            theme.nameToImage(upgrade.imageName),
            modifier = Modifier.height(192.dp)
        )
        Text(
            "Level ${toEngNotation(upgrade.level.toBigDecimal())}" +
                    if (upgrade.maxLevel != null) " / ${toEngNotation(upgrade.maxLevel.toBigDecimal())} " else "",
            style = theme.scaledStyles.smallBodyStyle,
            color = Color.White,
            textAlign = TextAlign.Center,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(32.dp)
        ) {
            if (upgrade.maxLevel?.let { upgrade.level < it } ?: true)
                Text(
                    "${toEngNotation(upgrade.price.toBigDecimal())} ${cake?.name.toString()}",
                    style = theme.scaledStyles.smallBodyStyle,
                    color = Color.White,
                ) else
                Text(
                    "Max Level Reached",
                    style = theme.scaledStyles.smallBodyStyle,
                    color = Color.White,
                )
        }
        LargeThemedButton(
            theme = theme,
            onClick = {
                buyUpgrade(upgrade)
            },
            enabled = (cake?.amount
                ?: BigDecimal.ZERO) >= upgrade.price && (upgrade.maxLevel?.let { upgrade.level < it } ?: true),
            modifier = Modifier.width(180.dp)
        ) {
            Text(
                "Buy"
            )
        }
    }
}

@Preview
@Composable
fun UpgradeDisplayPreview() {
    val theme = getDefaultTheme()
    val uiState = Save.state.copy(
        items = Save.state.items.map { it.copy(amount = 1000.toBigDecimal()) }
    )
    val upgrade = uiState.upgrades.first()
    UpgradeDisplay(
        theme = theme,
        uiState = uiState,
        buyUpgrade = {},
        upgrade = upgrade
    )
}