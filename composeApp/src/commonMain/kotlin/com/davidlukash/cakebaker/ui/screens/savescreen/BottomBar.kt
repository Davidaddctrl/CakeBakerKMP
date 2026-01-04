package com.davidlukash.cakebaker.ui.screens.savescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.SaveFile
import com.davidlukash.cakebaker.ui.LargeThemedButton

import com.davidlukash.cakebaker.ui.navigation.MenuScreen
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.withErrorHandling

@Composable
fun BottomBar() {
    val mainViewModel = LocalMainViewModel.current
    val themeViewModel = mainViewModel.themeViewModel
    val uiViewModel = mainViewModel.uiViewModel
    val theme by themeViewModel.theme.collectAsState()
    val dataViewModel = mainViewModel.dataViewModel
    val saveFileViewModel = mainViewModel.saveFileViewModel
    val coroutineScope = rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        LargeThemedButton(
            onClick = {
                uiViewModel.navigateWithFade(MenuScreen)
            },
            modifier = Modifier.width(280.dp)
        ) {
            Text("Back", style = theme.buttonTextStyle)
        }
        Spacer(
            modifier = Modifier.weight(1f)
        )
        LargeThemedButton(
            onClick = {
                //uiViewModel.navigateWithFade(MenuScreen)
            },
            modifier = Modifier.width(280.dp)
        ) {
            Text("Import", style = theme.buttonTextStyle)
        }
        LargeThemedButton(
            onClick = {
                val result = withErrorHandling(uiViewModel) {
                    saveFileViewModel.upsertSave(
                        SaveFile(
                            name = "save.json",
                            save = dataViewModel.createSave()
                        )
                    )
                }
                result.onSuccess {
                    uiViewModel.addTextPopup("Save Overwritten")
                }
                result.onFailure {
                    uiViewModel.addTextPopup("Save Error. Check debug console")
                }
            },
            modifier = Modifier.width(280.dp)
        ) {
            Text("Create", style = theme.buttonTextStyle)
        }
    }
}