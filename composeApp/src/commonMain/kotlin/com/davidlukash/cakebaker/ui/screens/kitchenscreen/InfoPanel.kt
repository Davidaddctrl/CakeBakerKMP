package com.davidlukash.cakebaker.ui.screens.kitchenscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.toEngNotation
import com.davidlukash.cakebaker.ui.container.PrimaryContainer
import com.davidlukash.cakebaker.ui.ResourceImage
import com.davidlukash.cakebaker.ui.input.SwitchButton
import com.davidlukash.jsonmath.createObject
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RowScope.InfoPanel(
    uiState: UIState, setAutoOvenEnabled: (Boolean) -> Unit,
    setAutoOrderCompleteEnabled: (Boolean) -> Unit
) {
    val satisfactionLevel by derivedStateOf { uiState.getSatisfactionLevel() }
    val satisfaction = uiState.customerSatisfaction
    val currentCakeTier = uiState.currentCakeTier
    val cakesSalePrices by derivedStateOf { uiState.getCakesSalesPrices() }
    val autoOven by derivedStateOf { uiState.getAutoOven() }
    val autoOrderComplete by derivedStateOf { uiState.getAutoOrderComplete() }
    PrimaryContainer(
        modifier = Modifier.weight(1f))
    {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Information",
                style = Theme.Styles.smallBodyStyle,
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
                    style = Theme.Styles.smallBodyStyle,
                    textAlign = TextAlign.Center,
                )
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ResourceImage(
                        data = when (satisfactionLevel) {
                            1 -> Theme.getImage("Sad Face")
                            2 -> Theme.getImage("Neutral Sad Face")
                            3 -> Theme.getImage("Neutral Face")
                            4 -> Theme.getImage("Medium Face")
                            5 -> Theme.getImage("Happy Face")
                            else -> Theme.getImage("Happy Face")
                        },
                        modifier = Modifier.size(36.dp),
                    )
                    Text(
                        "$satisfaction%",
                        style = Theme.Styles.smallBodyStyle,
                    )
                }
            }
            Text(
                "Cake Sale Price",
                style = Theme.Styles.smallBodyStyle,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "$${toEngNotation(cakesSalePrices[currentCakeTier] ?: BigDecimal.ZERO)}",
                style = Theme.Styles.largeBodyStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            autoOven?.let { autoOven ->
                Text(
                    "Auto Oven",
                    style = Theme.Styles.smallBodyStyle,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                SwitchButton(
                    value = autoOven.second,
                    onText = "On",
                    offText = "Off",
                    enabled = autoOven.first,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    setAutoOvenEnabled(it)
                }
            }

            autoOrderComplete?.let { autoOrderComplete ->
                Text(
                    "Auto Order Complete",
                    style = Theme.Styles.smallBodyStyle,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                SwitchButton(
                    value = autoOrderComplete.second,
                    onText = "On",
                    offText = "Off",
                    enabled = autoOrderComplete.first,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    setAutoOrderCompleteEnabled(it)
                }
            }
        }
    }
}

@Preview
@Composable
fun InfoPanelPreview() {
    var autoOvenEnabled by remember { mutableStateOf(true) }
    var autoOrderCompleteEnabled by remember { mutableStateOf(true) }
    val uiState = remember(autoOrderCompleteEnabled, autoOvenEnabled) {
        Save.state.copy(
            customerSatisfaction = 50,
            upgrades = Save.default.upgrades.filter { it.name == "Auto Oven" || it.name == "Auto Order Complete" }.map {
                (if (it.name == "Auto Oven")
                    it.copy(parameters = mapOf("enabled" to createObject(autoOvenEnabled)))
                else
                    it.copy(parameters = mapOf("enabled" to createObject(autoOrderCompleteEnabled)))).copy(level = 1)
            },
        )
    }
    Row(
        modifier = Modifier.size(400.dp, 720.dp)
    ) {
        InfoPanel(
            uiState = uiState,
            setAutoOvenEnabled = { autoOvenEnabled = it }
        ) { autoOrderCompleteEnabled = it }
    }
}