package com.davidlukash.cakebaker.ui.screens.kitchenscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import cakebaker.composeapp.generated.resources.Res
import cakebaker.composeapp.generated.resources.check
import cakebaker.composeapp.generated.resources.chevron_backward
import cakebaker.composeapp.generated.resources.chevron_forward
import cakebaker.composeapp.generated.resources.close
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.toEngNotation
import com.davidlukash.cakebaker.ui.container.PrimaryContainer

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import org.jetbrains.compose.resources.painterResource

@Composable
fun RowScope.RecipePanel(uiState: UIState, setCurrentCake: (Int) -> Unit) {
    val cakes by remember(uiState.items) { derivedStateOf { uiState.getCakes() } }
    val ingredients by remember(uiState.items) { derivedStateOf { uiState.getIngredients() } }
    val currentCakeTier = uiState.currentCakeTier

    PrimaryContainer(
        modifier = Modifier.weight(1f).fillMaxWidth(),
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth().height(72.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.chevron_backward),
                        contentDescription = Theme.getString("content_description.previous_tier"),
                        modifier = Modifier.size(64.dp).clip(CircleShape).clickable(
                            enabled = currentCakeTier != 1
                        ) {
                            setCurrentCake(currentCakeTier - 1)
                        },
                        tint = if (currentCakeTier != 1) LocalContentColor.current else Color.Transparent
                    )
                    Text(
                        cakes[currentCakeTier]?.name?.let {
                            Theme.getString(it)
                        } ?: Theme.getString("label.invalid_cake_tier"),
                        style = Theme.Styles.mediumBodyStyle,
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )
                    Icon(
                        painter = painterResource(Res.drawable.chevron_forward),
                        contentDescription = Theme.getString("content_description.next_tier"),
                        modifier = Modifier.size(64.dp).clip(CircleShape).clickable(
                            enabled = currentCakeTier != cakes.size
                        ) {
                            setCurrentCake(currentCakeTier + 1)
                        },
                        tint = if (currentCakeTier != cakes.size) LocalContentColor.current else Color.Transparent
                    )
                }
                ingredients.forEach { item ->
                    key(item.id) {
                        val cakePrice = item.cakePrices?.get(currentCakeTier) ?: BigDecimal.ZERO
                        if (cakePrice != BigDecimal.ZERO) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        "•",
                                        style = Theme.Styles.smallBodyStyle,
                                    )
                                    Text(
                                        "${toEngNotation(cakePrice)} ${Theme.getString(item.name)}",
                                        style = Theme.Styles.smallBodyStyle,
                                    )
                                }
                                if (item.amount >= cakePrice)
                                    Icon(
                                        painter = painterResource(Res.drawable.check),
                                        contentDescription = Theme.getString("content_description.enough"),
                                        tint = Theme.SuccessColor,
                                        modifier = Modifier.size(36.dp)
                                    )
                                else
                                    Icon(
                                        painter = painterResource(Res.drawable.close),
                                        contentDescription = Theme.getString("content_description.not_enough"),
                                        tint = Theme.DangerColor,
                                        modifier = Modifier.size(36.dp)
                                    )
                            }
                        }
                    }
                }
            }
        },
    )
}
