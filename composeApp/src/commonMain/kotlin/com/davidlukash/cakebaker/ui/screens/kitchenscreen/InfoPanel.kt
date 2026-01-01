package com.davidlukash.cakebaker.ui.screens.kitchenscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.toBoolean
import com.davidlukash.cakebaker.toEngNotation
import com.davidlukash.cakebaker.ui.Container
import com.davidlukash.cakebaker.ui.LocalFontFamily
import com.davidlukash.cakebaker.ui.ResourceImage
import com.davidlukash.cakebaker.ui.SwitchButton
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.ionspin.kotlin.bignum.decimal.BigDecimal

@Composable
fun RowScope.InfoPanel() {
    val mainViewModel = LocalMainViewModel.current
    val dataViewModel = mainViewModel.dataViewModel
    val satisfactionLevel by dataViewModel.satisfactionLevel.collectAsState(initial = null)
    val satisfaction by dataViewModel.customerSatisfaction.collectAsState()
    val autoOvenEnabled by dataViewModel.autoOvenEnabled.collectAsState()
    val themeViewModel = mainViewModel.themeViewModel
    val currentCakeTier by dataViewModel.currentCakeTier.collectAsState()
    val cakePrices by dataViewModel.cakePricesFlow.collectAsState(initial = emptyList())
    val theme by themeViewModel.theme.collectAsState()
    val upgrades by dataViewModel.upgradesFlow.collectAsState()
    val autoOven = upgrades.find { it.name == "Auto Oven" }?.level?.toBoolean() ?: false

    Container(
        modifier = Modifier.weight(1f).fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().height(720.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Information",
                fontFamily = LocalFontFamily.current,
                style = theme.labelStyle,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Customer\nSatisfaction",
                    fontFamily = LocalFontFamily.current,
                    style = theme.labelStyle,
                    textAlign = TextAlign.Center,
                )
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ResourceImage(
                        data = when (satisfactionLevel) {
                            1 -> theme.nameToImage("Sad Face")
                            2 -> theme.nameToImage("Neutral Sad Face")
                            3 -> theme.nameToImage("Neutral Face")
                            4 -> theme.nameToImage("Medium Face")
                            5 -> theme.nameToImage("Happy Face")
                            else -> theme.nameToImage("Happy Face")
                        },
                        modifier = Modifier.size(36.dp),
                    )
                    Text(
                        "$satisfaction%",
                        fontFamily = LocalFontFamily.current,
                        style = theme.labelStyle,
                    )
                }
            }
            Text(
                "Cake Sale Price",
                fontFamily = LocalFontFamily.current,
                style = theme.labelStyle,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "$${toEngNotation(cakePrices.getOrNull(currentCakeTier - 1) ?: BigDecimal.ZERO)}",
                fontFamily = LocalFontFamily.current,
                style = theme.smallTitleStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
            Text(
                "Auto Oven",
                fontFamily = LocalFontFamily.current,
                style = theme.labelStyle,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            SwitchButton(
                value = autoOvenEnabled,
                onText = "On",
                offText = "Off",
                enabled = autoOven,
                modifier = Modifier.fillMaxWidth()
            ) {
                dataViewModel.setAutoOvenEnabled(it)
            }
        }
    }
}
