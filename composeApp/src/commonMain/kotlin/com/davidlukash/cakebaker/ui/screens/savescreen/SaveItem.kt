package com.davidlukash.cakebaker.ui.screens.savescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import com.davidlukash.cakebaker.VERSION
import com.davidlukash.cakebaker.VERSIONCODE
import com.davidlukash.cakebaker.data.Save
import com.davidlukash.cakebaker.data.SaveFile
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.ui.Container
import com.davidlukash.cakebaker.ui.LargeThemedButton

import com.davidlukash.cakebaker.ui.navigation.KitchenScreen
import com.davidlukash.cakebaker.ui.navigation.transitionDuration
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.withErrorHandling
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

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
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                saveFile.name,
                style = theme.buttonTextStyle,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                "Version: ${saveFile.save.version}",
                style = theme.labelStyle,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                if (!saveFile.isDefault)
                    LargeThemedButton(
                        theme = theme,
                        onClick = {
                            deleteSave(saveFile)
                        },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("Delete", style = theme.buttonTextStyle)
                    }
                if (saveFile.save.versionCode != null && saveFile.save.versionCode <= VERSIONCODE)
                    LargeThemedButton(
                        theme = theme,
                        onClick = {
                            //exportSave(saveFile)
                        },
                        modifier = Modifier.weight(1f).defaultMinSize(minWidth = 600.dp)
                    ) {
                        Text("Migrate", style = theme.buttonTextStyle)
                    }
                LargeThemedButton(
                    theme = theme,
                    onClick = {
                        //deleteSave(saveFile)
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Rename", style = theme.buttonTextStyle)
                }
                LargeThemedButton(
                    theme = theme,
                    onClick = {
                        loadSave(saveFile)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Load", style = theme.buttonTextStyle)
                }
                if (!saveFile.isDefault)
                    LargeThemedButton(
                        theme = theme,
                        onClick = {
                            overwriteSave(saveFile)
                        },
                        modifier = Modifier.weight(1f).defaultMinSize(minWidth = 600.dp),
                    ) {
                        Text("Overwrite", style = theme.buttonTextStyle)
                    }
            }
        }
    }
}

@Preview(widthDp = 960)
@Composable
fun SaveItemPreview() {
    val theme = getDefaultTheme()
    SaveItem(
        theme = theme,
        exportSave = {},
        deleteSave = {},
        loadSave = {},
        overwriteSave = {},
        saveFile = SaveFile(name = "savepreview", save = Save.default.copy(
            version = "Unknown",
            versionCode = 2
        ))
    )
}