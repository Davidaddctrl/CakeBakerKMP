package com.davidlukash.cakebaker.ui.screens.kitchenscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.Save
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.Upgrade
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.toEngNotation
import com.davidlukash.cakebaker.ui.Container

import com.davidlukash.cakebaker.ui.ResourceImage
import com.davidlukash.cakebaker.ui.SwitchButton
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RowScope.InfoPanel(theme: Theme, uiState: UIState, setAutoOvenEnabled: (Boolean) -> Unit) {
    val satisfactionLevel = uiState.getSatisfactionLevel()
    val satisfaction = uiState.customerSatisfaction
    val autoOvenEnabled = uiState.autoOvenEnabled
    val currentCakeTier = uiState.currentCakeTier
    val cakesSalePrices = uiState.getCakesSalesPrices()
    val autoOven = uiState.getAutoOven()

    Container(
        theme = theme,
        modifier = Modifier.weight(1f).fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().height(720.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Information",
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
                        style = theme.labelStyle,
                    )
                }
            }
            Text(
                "Cake Sale Price",
                style = theme.labelStyle,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "$${toEngNotation(cakesSalePrices[currentCakeTier] ?: BigDecimal.ZERO)}",
                style = theme.smallTitleStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            autoOven?.let {
                Text(
                    "Auto Oven",
                    style = theme.labelStyle,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                SwitchButton(
                    theme = theme,
                    value = autoOvenEnabled,
                    onText = "On",
                    offText = "Off",
                    enabled = autoOven,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    setAutoOvenEnabled(it)
                }
            }
        }
    }
}

@Preview
@Composable
fun InfoPanelPreview() {
    val theme = getDefaultTheme()
    var autoOvenEnabled by remember { mutableStateOf(true) }
    val uiState = Save.state.copy(
        customerSatisfaction = 50,
        upgrades = Save.default.upgrades.filter { it.name == "Auto Oven" }.map {
            it.copy(level = 1)
        },
        autoOvenEnabled = autoOvenEnabled
    )
    Row(
        modifier = Modifier.size(400.dp, 720.dp)
    ) {
        InfoPanel(
            theme = theme,
            uiState = uiState,
            setAutoOvenEnabled = { autoOvenEnabled = it }
        )
    }
}