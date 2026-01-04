package com.davidlukash.cakebaker.ui.screens.savescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.SaveFile
import com.davidlukash.cakebaker.ui.Container
import com.davidlukash.cakebaker.ui.LargeThemedButton

import com.davidlukash.cakebaker.ui.navigation.KitchenScreen
import com.davidlukash.cakebaker.ui.navigation.transitionDuration
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.withErrorHandling
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SaveItem(saveFile: SaveFile) {
    val mainViewModel = LocalMainViewModel.current
    val themeViewModel = mainViewModel.themeViewModel
    val dataViewModel = mainViewModel.dataViewModel
    val saveFileViewModel = mainViewModel.saveFileViewModel
    val uiViewModel = mainViewModel.uiViewModel
    val theme by themeViewModel.theme.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Container(
        modifier = Modifier.fillMaxWidth().height(360.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                saveFile.name,
                style = theme.buttonTextStyle,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                LargeThemedButton(
                    onClick = {
                        saveFileViewModel.exportSave(saveFile)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Export", style = theme.buttonTextStyle)
                }
                LargeThemedButton(
                    onClick = {
                        saveFileViewModel.deleteSave(saveFile.name)
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !saveFile.isDefault
                ) {
                    Text("Delete", style = theme.buttonTextStyle)
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                LargeThemedButton(
                    onClick = {
                        coroutineScope.launch {
                            dataViewModel.loadSave(saveFile.save)
                            uiViewModel.navigateWithFade(KitchenScreen)
                            delay(transitionDuration.toLong())
                            uiViewModel.addTextPopup("Save Loaded")
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Load", style = theme.buttonTextStyle)
                }
                LargeThemedButton(
                    onClick = {
                        val result = withErrorHandling(uiViewModel) {
                            saveFileViewModel.upsertSave(
                                saveFile.copy(
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
                    modifier = Modifier.weight(1f),
                    enabled = !saveFile.isDefault
                ) {
                    Text("Overwrite", style = theme.buttonTextStyle)
                }
            }
        }
    }
}