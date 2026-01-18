package com.davidlukash.cakebaker.ui.screens.upgradescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.ItemTopRow

import com.davidlukash.cakebaker.ui.input.MenuButton
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TopBar(theme: Theme, uiState: UIState) {
    MenuButton {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Upgrade Shop",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = theme.scaledStyles.titleStyle,
            )
            ItemTopRow(theme, uiState)
        }
    }
}

@Preview(
    widthDp = 1920,
)
@Composable
fun TopBarPreview() {
    val theme = Theme.default
    val uiState = Save.state
    TopBar(theme, uiState)
}