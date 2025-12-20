package com.davidlukash.cakebaker.ui.screens.ingredientscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.ui.ImageButton
import com.davidlukash.cakebaker.ui.ResourceImage
import com.davidlukash.cakebaker.ui.navigation.Screen
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel

@Composable
fun BottomBar() {
    val mainViewModel = LocalMainViewModel.current
    val themeViewModel = mainViewModel.themeViewModel
    val uiViewModel = mainViewModel.uiViewModel
    val theme by themeViewModel.theme.collectAsState()
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
    ) {
        ImageButton(
            onClick = {
                uiViewModel.navigateWithFade(Screen.Kitchen)
            },
        ) {
            ResourceImage(
                theme.nameToImage("Oven"),
                modifier = Modifier.height(320.dp)
            )
        }

        ImageButton(
            onClick = {
                uiViewModel.navigateWithFade(Screen.Upgrade)
            },
        ) {
            ResourceImage(
                theme.nameToImage("Upgrade Shop"),
                modifier = Modifier.height(320.dp)
            )
        }
    }
}