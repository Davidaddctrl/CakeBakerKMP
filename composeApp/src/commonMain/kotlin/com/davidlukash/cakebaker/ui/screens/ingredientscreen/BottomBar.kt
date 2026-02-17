package com.davidlukash.cakebaker.ui.screens.ingredientscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.input.ImageButton
import com.davidlukash.cakebaker.ui.ResourceImage
import com.davidlukash.cakebaker.ui.navigation.KitchenScreen
import com.davidlukash.cakebaker.ui.navigation.Screen
import com.davidlukash.cakebaker.ui.navigation.UpgradeScreen

@Composable
fun BottomBar(navigateWithFade: (Screen) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
    ) {
        ImageButton(
            onClick = {
                navigateWithFade(KitchenScreen)
            },
        ) {
            ResourceImage(
                Theme.getImage("image.oven"),
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.height(280.dp)
            )
        }

        ImageButton(
            onClick = {
                navigateWithFade(UpgradeScreen)
            },
        ) {
            ResourceImage(
                Theme.getImage("image.upgrade_shop"),
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.height(280.dp)
            )
        }
    }
}