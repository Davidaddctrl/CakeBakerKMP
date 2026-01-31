package com.davidlukash.cakebaker.ui.screens.kitchenscreen

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
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
import com.davidlukash.cakebaker.platformui.HorizontalArrangement
import com.davidlukash.cakebaker.platformui.ImageResource
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.VerticalArrangement
import com.davidlukash.cakebaker.platformui.ui.Column
import com.davidlukash.cakebaker.platformui.ui.Icon
import com.davidlukash.cakebaker.platformui.ui.Row
import com.davidlukash.cakebaker.platformui.ui.Text
import com.davidlukash.cakebaker.toEngNotation
import com.davidlukash.cakebaker.ui.container.PrimaryContainer
import com.davidlukash.cakebaker.ui.input.TransparentButton
import com.ionspin.kotlin.bignum.decimal.BigDecimal

@Composable
fun RecipePanel(modifier: Modifier, uiState: UIState, setCurrentCake: (Int) -> Unit) {
    val cakes  by derivedStateOf { uiState.getCakes() }
    val ingredients by derivedStateOf { uiState.getIngredients() }
    val currentCakeTier = uiState.currentCakeTier

    PrimaryContainer(
        modifier = modifier,
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = VerticalArrangement.SpacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = HorizontalArrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth().height(72.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TransparentButton(
                        onClick = { setCurrentCake(currentCakeTier - 1) },
                        modifier = Modifier.size(64.dp),
                        shapeRadius = 32.dp,
                        padding = 0.dp,
                        enabled = currentCakeTier != 1
                    ) {
                        Icon(
                            ImageResource(Res.drawable.chevron_backward, "chevron_backward_svg.svg"),
                            contentDescription = "Next Cake Tier",
                            tint = if (currentCakeTier != 1) LocalContentColor.current else Color.Transparent,
                            modifier = Modifier.size(64.dp),
                        )
                    }
                    Text(
                        cakes[currentCakeTier]?.name ?: "Cake Tier Invalid",
                        style = Theme.Styles.mediumBodyStyle.copy(
                            textAlign = TextAlign.Center,
                            textDecoration = TextDecoration.Underline,
                        ),
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )
                    TransparentButton(
                        onClick = { setCurrentCake(currentCakeTier + 1) },
                        modifier = Modifier.size(64.dp),
                        shapeRadius = 32.dp,
                        padding = 0.dp,
                        enabled = currentCakeTier != cakes.size
                    ) {
                        Icon(
                            ImageResource(Res.drawable.chevron_forward, "chevron_forward_svg.svg"),
                            contentDescription = "Next Cake Tier",
                            tint = if (currentCakeTier != cakes.size) LocalContentColor.current else Color.Transparent,
                            modifier = Modifier.size(64.dp),
                        )
                    }
                }
                ingredients.forEach { item ->
                    key(item.name) {
                        val cakePrice = item.cakePrices?.get(currentCakeTier) ?: BigDecimal.ZERO
                        if (cakePrice != BigDecimal.ZERO) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Text(
                                        "•",
                                        style = Theme.Styles.smallBodyStyle,
                                    )
                                    Text(
                                        "${toEngNotation(cakePrice)} ${item.name}",
                                        style = Theme.Styles.smallBodyStyle
                                    )
                                }
                                if (item.amount >= cakePrice)
                                    Icon(
                                        imageResource = ImageResource(Res.drawable.check, "check_svg.svg"),
                                        contentDescription = "Enough",
                                        tint = Theme.SuccessColor,
                                        modifier = Modifier.size(36.dp)
                                    )
                                else
                                    Icon(
                                        imageResource = ImageResource(Res.drawable.close, "close_svg.svg"),
                                        contentDescription = "Not Enough",
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
