package com.davidlukash.cakebaker.ui.screens.savescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.VERSIONCODE
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.save.SaveFile
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.container.PrimaryContainer
import com.davidlukash.cakebaker.ui.input.LargeThemedButton

import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SaveItem(
    modifier: Modifier = Modifier,
    exportSave: (SaveFile) -> Unit,
    deleteSave: (SaveFile) -> Unit,
    loadSave: (SaveFile) -> Unit,
    overwriteSave: (SaveFile) -> Unit,
    saveFile: SaveFile
) {
    PrimaryContainer(
        modifier = modifier.fillMaxWidth(),
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    saveFile.name,
                    style = Theme.Styles.titleStyle,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(
                    Theme.getString("label.version").replace("{0}", saveFile.save.version),
                    style = Theme.Styles.smallBodyStyle,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LargeThemedButton(
                        onClick = {
                            exportSave(saveFile)
                        },
                        modifier = Modifier.weight(1f).defaultMinSize(minWidth = Theme.Styles.buttonTextStyle.fontSize.value.dp * 6),
                        content = {
                            Text(Theme.getString("action.export"), maxLines = 1, softWrap = false)
                        }
                    )
                    if (!saveFile.isDefault)
                        LargeThemedButton(
                            onClick = {
                                deleteSave(saveFile)
                            },
                            modifier = Modifier.weight(1f),
                            content = {
                                Text(Theme.getString("action.delete"), maxLines = 1, softWrap = false)
                            },
                        )
                    LargeThemedButton(
                        onClick = {
                            loadSave(saveFile)
                        },
                        modifier = Modifier.weight(1f),
                        content = {
                            Text(Theme.getString("action.load"), maxLines = 1, softWrap = false)
                        }
                    )
                    if (!saveFile.isDefault)
                        LargeThemedButton(
                            onClick = {
                                overwriteSave(saveFile)
                            },
                            modifier = Modifier.weight(1f),
                            content = {
                                Text(Theme.getString("action.overwrite"), maxLines = 1, softWrap = false)
                            },
                        )
                }
            }
        }
    )
}

@Preview(widthDp = 960)
@Composable
fun SaveItemPreview() {
    SaveItem(
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