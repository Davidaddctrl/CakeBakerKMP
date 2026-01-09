package com.davidlukash.cakebaker.ui.screens.kitchenscreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.Order
import com.davidlukash.cakebaker.data.Save
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.ui.ImageButton

import com.davidlukash.cakebaker.ui.ProgressBar
import com.davidlukash.cakebaker.ui.ResourceImage
import com.davidlukash.cakebaker.ui.navigation.IngredientScreen
import com.davidlukash.cakebaker.ui.navigation.Screen
import com.davidlukash.cakebaker.ui.navigation.UpgradeScreen
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.floor

@Composable
fun MainContent(
    theme: Theme,
    uiState: UIState,
    navigateWithFade: (Screen) -> Unit,
    bake: () -> Unit,
    setAutoOvenEnabled: (Boolean) -> Unit,
    completeOrder: (Order) -> Unit,
    setCurrentCake: (Int) -> Unit,
    innerPadding: PaddingValues
) {
    val progress = uiState.ovenProgress
    val canBake = uiState.canBake
    val ovenRunning = uiState.ovenRunning
    val fasterOvenLevel = uiState.getFasterOven()
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize().padding(innerPadding).padding(top = 16.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                ProgressBar(theme, progress)
                val ovenTime = 5.0 - fasterOvenLevel / 10.0
                if (ovenRunning)
                    Text(
                        "${floor((1.0 - progress) * ovenTime * 10.0) / 10.0} seconds remaining",
                        style = theme.smallLabelStyle,
                        color = theme.buttonTheme.contentColor
                    )
            }
            ImageButton(
                onClick = {
                    bake()
                },
                enabled = canBake && !ovenRunning
            ) {
                ResourceImage(
                    theme.nameToImage("Oven"),
                    modifier = Modifier.height(280.dp)
                )
            }
            ImageButton(
                onClick = {
                    navigateWithFade(IngredientScreen)
                }
            ) {
                ResourceImage(
                    theme.nameToImage("Ingredient Shop"),
                    modifier = Modifier.height(280.dp)
                )
            }
        }
        RecipePanel(theme, uiState, setCurrentCake)
        OrdersPanel(theme, uiState, completeOrder)
        InfoPanel(theme, uiState, setAutoOvenEnabled)
        ImageButton(
            onClick = {
                navigateWithFade(UpgradeScreen)
            }
        ) {
            ResourceImage(
                theme.nameToImage("Upgrade Shop"),
                modifier = Modifier.height(280.dp)
            )
        }
    }
}

@Preview(
    widthDp = 1920,
    heightDp = 1080
)
@Composable
fun MainContentPreview() {
    val theme = getDefaultTheme()
    var autoOvenEnabled by remember { mutableStateOf(true) }
    val infiniteTransition = rememberInfiniteTransition()
    val amount by infiniteTransition.animateFloat(
        0f, 1f, animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
        )
    )
    val uiState = Save.state.copy(
        ovenRunning = true,
        ovenProgress = amount.toDouble(),
        customerSatisfaction = 50,
        upgrades = Save.default.upgrades.filter { it.name == "Auto Oven" }.map {
            it.copy(level = 1)
        },
        autoOvenEnabled = autoOvenEnabled
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
            completeOrder = { },
            setCurrentCake = {},
            innerPadding = PaddingValues(16.dp)
        )
    }
}