package com.davidlukash.cakebaker.ui.screens.upgradescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.ui.Container
import com.davidlukash.cakebaker.ui.ImageButton

import com.davidlukash.cakebaker.ui.ResourceImage
import com.davidlukash.cakebaker.ui.navigation.IngredientScreen
import com.davidlukash.cakebaker.ui.navigation.KitchenScreen
import com.davidlukash.cakebaker.ui.navigation.Screen
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel

@Composable
fun BottomBar(currentPage: String, setCurrentPage: (String) -> Unit) {
    val mainViewModel = LocalMainViewModel.current
    val themeViewModel = mainViewModel.themeViewModel
    val uiViewModel = mainViewModel.uiViewModel
    val dataViewModel = mainViewModel.dataViewModel
    val theme by themeViewModel.theme.collectAsState()
    val upgrades by dataViewModel.upgradesFlow.collectAsState()
    val pages = upgrades.map { it.pageName }.distinct()
    LaunchedEffect(pages) {
        setCurrentPage(pages.firstOrNull() ?: "")
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
    ) {

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
        Container(
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp).height(280.dp),
        ) {
            FlowRow(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pages.forEach { page ->
                    key(page) {
                        ImageButton(
                            onClick = {
                                setCurrentPage(page)
                            },
                            modifier = Modifier.defaultMinSize(minWidth = 200.dp, minHeight = 80.dp).weight(1f)
                        ) {
                            Text(
                                page,
                                textAlign = TextAlign.Center,
                                style = theme.labelStyle,
                                fontWeight = if (page == currentPage) FontWeight.Bold else FontWeight.Normal,
                                color = if (page == currentPage) theme.buttonTheme.containerColor else Color.Black,
                            )
                        }
                    }
                }
            }
        }
        ImageButton(
            onClick = {
                uiViewModel.navigateWithFade(KitchenScreen)
            }
        ) {
            ResourceImage(
                theme.nameToImage("Oven"),
                modifier = Modifier.height(280.dp)
            )
        }
    }
}