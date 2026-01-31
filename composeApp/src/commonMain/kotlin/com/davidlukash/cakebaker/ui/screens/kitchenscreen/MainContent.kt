package com.davidlukash.cakebaker.ui.screens.kitchenscreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.order.Order
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.ui.Box
import com.davidlukash.cakebaker.platformui.ui.Column
import com.davidlukash.cakebaker.platformui.ui.Row
import com.davidlukash.cakebaker.platformui.ui.Spacer
import com.davidlukash.cakebaker.platformui.ui.Text
import com.davidlukash.cakebaker.ui.ProgressBar
import com.davidlukash.cakebaker.ui.ResourceImage
import com.davidlukash.cakebaker.ui.input.TransparentButton
import com.davidlukash.cakebaker.ui.navigation.IngredientScreen
import com.davidlukash.cakebaker.ui.navigation.Screen
import com.davidlukash.cakebaker.ui.navigation.UpgradeScreen
import kotlin.math.floor


@Composable
fun MainContent(
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
    Row(
        modifier = Modifier.fillMaxSize()
            //.padding(innerPadding)
            .padding(top = 16.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
            ) {
                key(ovenRunning, ovenProgress) {
                    ProgressBar(
                        modifier = Modifier.width(296.dp).align(Alignment.Center),
                        ovenProgress
                    )
                    val ovenTime = 5.0 - fasterOvenLevel / 10.0
                    if (ovenRunning) {
                        CompositionLocalProvider(
                            LocalContentColor provides Theme.ProgressBarTheme.contentColor
                        ) {
                            Text(
                                "${floor((1.0 - ovenProgress) * ovenTime * 10.0) / 10.0} seconds remaining",
                                style = Theme.Styles.verySmallBodyStyle,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
            TransparentButton(
                onClick = {
                    bake()
                },
                enabled = canBake && !ovenRunning,
                modifier = Modifier
            ) {
                ResourceImage(
                    Theme.getImage("Oven"),
                    contentDescription = "Oven",
                    modifier = Modifier.height(280.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            TransparentButton(
                onClick = {
                    navigateWithFade(IngredientScreen)
                },
                modifier = Modifier.align(Alignment.Start)
            ) {
                ResourceImage(
                    Theme.getImage("Ingredient Shop"),
                    contentDescription = "Ingredient Shop",
                    modifier = Modifier.height(280.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        RecipePanel(Modifier.weight(1f), uiState, setCurrentCake)
//        Spacer(modifier = Modifier.width(16.dp))
//        OrdersPanel(uiState, completeOrder, nextOrderRemainingTime, orders)
//        Spacer(modifier = Modifier.width(16.dp))
//        InfoPanel(
//            uiState, setAutoOvenEnabled, setAutoOrderCompleteEnabled
//        )
        Spacer(modifier = Modifier.width(8.dp))
        TransparentButton(
            onClick = {
                navigateWithFade(UpgradeScreen)
            },
            modifier = Modifier
        ) {
            ResourceImage(
                Theme.getImage("Upgrade Shop"),
                contentDescription = "Upgrade Shop",
                modifier = Modifier.height(280.dp)
            )
        }
    }
}

//@Preview(
//    widthDp = 1920,
//    heightDp = 1080
//)
//@Composable
//fun MainContentPreview() {
//    var autoOvenEnabled by remember { mutableStateOf(true) }
//    var autoOrderCompleteEnabled by remember { mutableStateOf(true) }
//    val infiniteTransition = rememberInfiniteTransition()
//    val amount by infiniteTransition.animateFloat(
//        0f, 1f, animationSpec = infiniteRepeatable(
//            animation = tween(5000, easing = LinearEasing),
//        )
//    )
//    val orderRemainingTime by infiniteTransition.animateFloat(
//        120f, 0f, animationSpec = infiniteRepeatable(
//            animation = tween(120000, easing = LinearEasing),
//        )
//    )
//    val uiState = Save.state.copy(
//        customerSatisfaction = 50,
//        upgrades = Save.default.upgrades.filter { it.name == "Auto Oven" || it.name == "Auto Order Complete" }.map {
//            it.copy(level = 1)
//        },
//    )
//    Background {
//        MainContent(
//            uiState = uiState,
//            navigateWithFade = { },
//            bake = { },
//            setAutoOvenEnabled = { autoOvenEnabled = it },
//            setAutoOrderCompleteEnabled = { autoOrderCompleteEnabled = it },
//            completeOrder = { },
//            setCurrentCake = {},
//            ovenProgress = amount.toDouble(),
//            ovenRunning = true,
//            nextOrderRemainingTime = 30.0,
//            orders = listOf(
//                Order(
//                    cakeTier = 1,
//                    amount = 1,
//                    salePrice = 1500.toBigDecimal(),
//                    remainingTime = orderRemainingTime.toDouble(),
//                    totalTime = 120.0,
//                )
//            ),
//            innerPadding = PaddingValues(16.dp),
//        )
//    }
//}