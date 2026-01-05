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
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.Container
import com.davidlukash.cakebaker.ui.LargeThemedButton

import com.davidlukash.cakebaker.ui.navigation.KitchenScreen
import com.davidlukash.cakebaker.ui.navigation.transitionDuration
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.withErrorHandling
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SaveItem(
    theme: Theme,
    exportSave: (SaveFile) -> Unit,
    deleteSave: (SaveFile) -> Unit,
    loadSave: (SaveFile) -> Unit,
    overwriteSave: (SaveFile) -> Unit,
    saveFile: SaveFile
) {
    Container(
        theme = theme,
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
                    theme = theme,
                    onClick = {
                        exportSave(saveFile)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Export", style = theme.buttonTextStyle)
                }
                LargeThemedButton(
                    theme = theme,
                    onClick = {
                        deleteSave(saveFile)
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
                    theme = theme,
                    onClick = {
                        loadSave(saveFile)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Load", style = theme.buttonTextStyle)
                }
                LargeThemedButton(
                    theme = theme,
                    onClick = {
                        overwriteSave(saveFile)
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