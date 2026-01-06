package com.davidlukash.cakebaker.ui.screens.otherscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.LargeThemedButton
import com.davidlukash.cakebaker.ui.navigation.MenuScreen
import com.davidlukash.cakebaker.ui.navigation.Screen

@Composable
fun BottomBar(theme: Theme, navigateWithFade: (Screen) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        LargeThemedButton(
            theme = theme,
            onClick = {
                navigateWithFade(MenuScreen)
            },
            modifier = Modifier.width(280.dp)
        ) {
            Text("Back", style = theme.buttonTextStyle)
        }
    }
}