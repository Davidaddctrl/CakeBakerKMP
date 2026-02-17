package com.davidlukash.cakebaker.ui.screens.upgradescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.container.PrimaryContainer
import com.davidlukash.cakebaker.ui.input.ImageButton

import com.davidlukash.cakebaker.ui.ResourceImage
import com.davidlukash.cakebaker.ui.navigation.IngredientScreen
import com.davidlukash.cakebaker.ui.navigation.KitchenScreen
import com.davidlukash.cakebaker.ui.navigation.Screen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BottomBar(
    uiState: UIState,
    navigateWithFade: (Screen) -> Unit,
    currentPage: String,
    setCurrentPage: (String) -> Unit
) {
    val upgrades = uiState.upgrades
    val pages = upgrades.map { it.pageName }.distinct()
    LaunchedEffect(pages) {
        if (!pages.contains(currentPage)) {
            setCurrentPage(pages.firstOrNull() ?: "")
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
    ) {
        ImageButton(
            onClick = {
                navigateWithFade(IngredientScreen)
            }
        ) {
            ResourceImage(
                Theme.getImage("image.ingredient_shop"),
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.height(280.dp)
            )
        }
        PrimaryContainer(
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp).height(312.dp),
            content = {
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
                                    style = Theme.Styles.smallBodyStyle,
                                    fontWeight = if (page == currentPage) FontWeight.Bold else FontWeight.Normal,
                                    color = if (page == currentPage)
                                        Theme.TabSelectedColor
                                    else LocalContentColor.current,
                                )
                            }
                        }
                    }
                }
            },
        )
        ImageButton(
            onClick = {
                navigateWithFade(KitchenScreen)
            }
        ) {
            ResourceImage(
                Theme.getImage("image.oven"),
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.height(280.dp)
            )
        }
    }
}

@Preview(
    widthDp = 1920
)
@Composable
fun BottomBarPreview() {
    val uiState = Save.state
    var currentPage by remember { mutableStateOf("") }
    BottomBar(
        uiState = uiState,
        navigateWithFade = {},
        currentPage = currentPage,
    ) { currentPage = it }
}