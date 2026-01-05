package com.davidlukash.cakebaker.ui.screens.upgradescreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.ImageData
import com.davidlukash.cakebaker.data.Item
import com.davidlukash.cakebaker.data.ItemType
import com.davidlukash.cakebaker.data.Upgrade
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.toEngNotation
import com.davidlukash.cakebaker.ui.Container
import com.davidlukash.cakebaker.ui.LargeThemedButton

import com.davidlukash.cakebaker.ui.ResourceImage
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal

@Composable
fun UpgradeDisplay(theme: Theme, upgrade: Upgrade) {
    val mainViewModel = LocalMainViewModel.current
    val dataViewModel = mainViewModel.dataViewModel
    val cakes by dataViewModel.cakesFlow.collectAsState(initial = emptyMap())
    val cake = cakes[upgrade.cakeTier]
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp).width(480.dp)
    ) {
        Text(
            upgrade.name,
            style = theme.labelStyle,
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
            style = theme.labelStyle,
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
                    style = theme.labelStyle,
                    color = Color.White,
                ) else
                Text(
                    "Max Level Reached",
                    style = theme.labelStyle,
                    color = Color.White,
                )
        }
        LargeThemedButton(
            theme = theme,
            onClick = {
                dataViewModel.buyUpgrade(upgrade)
            },
            enabled = (cake?.amount
                ?: BigDecimal.ZERO) >= upgrade.price && (upgrade.maxLevel?.let { upgrade.level < it } ?: true),
            modifier = Modifier.width(180.dp)
        ) {
            Text(
                "Buy",
                style = theme.buttonTextStyle,
            )
        }
    }
}