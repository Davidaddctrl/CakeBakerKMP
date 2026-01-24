package com.davidlukash.cakebaker.ui.screens.kitchenscreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.davidlukash.cakebaker.data.order.Order
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.input.ImageButton
import com.davidlukash.cakebaker.ui.ProgressBar
import com.davidlukash.cakebaker.ui.ResourceImage
import com.davidlukash.cakebaker.ui.navigation.IngredientScreen
import com.davidlukash.cakebaker.ui.navigation.Screen
import com.davidlukash.cakebaker.ui.navigation.UpgradeScreen
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.floor

@Composable
fun MainContent(
    theme: Theme,
    uiState: UIState,
    navigateWithFade: (Screen) -> Unit,
    bake: () -> Unit,
    setAutoOvenEnabled: (Boolean) -> Unit,
    setAutoOrderCompleteEnabled: (Boolean) -> Unit,
    completeOrder: (Order) -> Unit,
    setCurrentCake: (Int) -> Unit,
    ovenProgress: Double,
    ovenRunning: Boolean,
    nextOrderRemainingTime: Double?,
    orders: List<Order>,
    innerPadding: PaddingValues
) {
    val canBake = uiState.canBake
    val fasterOvenLevel by derivedStateOf { uiState.getFasterOven() }
    val density = LocalDensity.current
    Row(
        modifier = Modifier.fillMaxSize().padding(innerPadding).padding(top = 16.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight()
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                ProgressBar(
                    theme,
                    modifier = Modifier.width(296.dp),
                    ovenProgress
                )
                val ovenTime = 5.0 - fasterOvenLevel / 10.0
                if (ovenRunning)
                    Text(
                        "${floor((1.0 - ovenProgress) * ovenTime * 10.0) / 10.0} seconds remaining",
                        style = theme.scaledStyles.verySmallBodyStyle,
                        color = theme.buttonTheme.contentColor
                    )
            }
            ImageButton(
                onClick = {
                    bake()
                },
                enabled = canBake && !ovenRunning,
            ) {
                ResourceImage(
                    theme.nameToImage("Oven"),
                    modifier = Modifier.height(280.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            ImageButton(
                onClick = {
                    navigateWithFade(IngredientScreen)
                },
                modifier = Modifier.align(Alignment.Start)
            ) {
                ResourceImage(
                    theme.nameToImage("Ingredient Shop"),
                    modifier = Modifier.height(280.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        RecipePanel(theme, uiState, setCurrentCake)
        Spacer(modifier = Modifier.width(16.dp))
        OrdersPanel(theme, uiState, completeOrder, nextOrderRemainingTime, orders)
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.BottomEnd
        ) {
            var buttonSize by remember { mutableStateOf(Size.Zero) }
            InfoPanel(
                theme, uiState, setAutoOvenEnabled, setAutoOrderCompleteEnabled, buttonSize.copy(
                width = buttonSize.width + density.run { 8.dp.toPx() },
                height = buttonSize.height + density.run { 8.dp.toPx() }
            ))
            ImageButton(
                onClick = {
                    navigateWithFade(UpgradeScreen)
                },
                modifier = Modifier.onGloballyPositioned {
                    buttonSize = it.size.toSize()
                }
            ) {
                ResourceImage(
                    theme.nameToImage("Upgrade Shop"),
                    modifier = Modifier.height(280.dp)
                )
            }
        }
    }
}

@Preview(
    widthDp = 1920,
    heightDp = 1080
)
@Composable
fun MainContentPreview() {
    val theme = Theme.default
    var autoOvenEnabled by remember { mutableStateOf(true) }
    var autoOrderCompleteEnabled by remember { mutableStateOf(true) }
    val infiniteTransition = rememberInfiniteTransition()
    val amount by infiniteTransition.animateFloat(
        0f, 1f, animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
        )
    )
    val orderRemainingTime by infiniteTransition.animateFloat(
        120f, 0f, animationSpec = infiniteRepeatable(
            animation = tween(120000, easing = LinearEasing),
        )
    )
    val uiState = Save.state.copy(
        customerSatisfaction = 50,
        upgrades = Save.default.upgrades.filter { it.name == "Auto Oven" || it.name == "Auto Order Complete" }.map {
            it.copy(level = 1)
        },
        autoOvenEnabled = autoOvenEnabled,
        autoOrderCompleteEnabled = autoOrderCompleteEnabled
    )
    Box(
        modifier = Modifier.fillMaxSize().background(theme.backgroundTheme.containerColor),
    ) {
        MainContent(
            theme = theme,
            uiState = uiState,
            navigateWithFade = { },
            bake = { },
            setAutoOvenEnabled = { autoOvenEnabled = it },
            setAutoOrderCompleteEnabled = { autoOrderCompleteEnabled = it },
            completeOrder = { },
            setCurrentCake = {},
            innerPadding = PaddingValues(16.dp),
            ovenRunning = true,
            ovenProgress = amount.toDouble(),
            nextOrderRemainingTime = 30.0,
            orders = listOf(
                Order(
                    cakeTier = 1,
                    amount = 1,
                    salePrice = 1500.toBigDecimal(),
                    remainingTime = orderRemainingTime.toDouble(),
                    totalTime = 120.0,
                )
            ),
        )
    }
}