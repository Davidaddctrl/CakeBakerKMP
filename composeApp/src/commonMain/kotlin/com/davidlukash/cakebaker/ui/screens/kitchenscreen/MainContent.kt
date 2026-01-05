package com.davidlukash.cakebaker.ui.screens.kitchenscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.Container
import com.davidlukash.cakebaker.ui.ImageButton

import com.davidlukash.cakebaker.ui.ProgressBar
import com.davidlukash.cakebaker.ui.ResourceImage
import com.davidlukash.cakebaker.ui.navigation.IngredientScreen
import com.davidlukash.cakebaker.ui.navigation.Screen
import com.davidlukash.cakebaker.ui.navigation.UpgradeScreen
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import kotlin.math.floor

@Composable
fun MainContent(theme: Theme, innerPadding: PaddingValues) {
    val mainViewModel = LocalMainViewModel.current
    val dataViewModel = mainViewModel.dataViewModel
    val uiViewModel = mainViewModel.uiViewModel
    val progress by dataViewModel.ovenProgress.collectAsState()
    val canBake by dataViewModel.canBakeFlow.collectAsState(initial = false)
    val ovenRunning by dataViewModel.ovenRunning.collectAsState(initial = true)
    val upgrades by dataViewModel.upgradesFlow.collectAsState(initial = emptyList())
    val fasterOvenLevel = upgrades.find { it.name == "Faster Oven" }?.level?.toDouble() ?: 0.0
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize().padding(innerPadding).padding(top = 16.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
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
                    dataViewModel.bake()
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
                    uiViewModel.navigateWithFade(IngredientScreen)
                }
            ) {
                ResourceImage(
                    theme.nameToImage("Ingredient Shop"),
                    modifier = Modifier.height(280.dp)
                )
            }
        }
        RecipePanel(theme)
        OrdersPanel(theme)
        InfoPanel(theme)
        ImageButton(
            onClick = {
                uiViewModel.navigateWithFade(UpgradeScreen)
            }
        ) {
            ResourceImage(
                theme.nameToImage("Upgrade Shop"),
                modifier = Modifier.height(280.dp)
            )
        }
    }
}