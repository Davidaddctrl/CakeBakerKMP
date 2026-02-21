package com.davidlukash.cakebaker.ui.screens.savescreen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.VERSIONCODE
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.save.SaveFile
import com.davidlukash.cakebaker.ui.navigation.Screen

@Composable
fun SaveScreen(
    saveFiles: List<SaveFile>,
    listSaves: () -> Unit,
    navigateWithFade: (Screen) -> Unit,
    exportSave: (SaveFile) -> Unit,
    deleteSave: (SaveFile) -> Unit,
    loadSave: (SaveFile) -> Unit,
    loadWithMigration: (SaveFile) -> Unit,
    overwriteSave: (SaveFile) -> Unit,
    importSave: () -> Unit
) {
    LaunchedEffect(Unit) {
        listSaves()
    }

    var dialogState by remember { mutableStateOf<SaveDialogState>(SaveDialogState.None) }
    when (val state = dialogState) {
        SaveDialogState.None -> {}
        is SaveDialogState.Load -> {
            val saveFile = state.saveFile
            val canMigrate = if (saveFile.save.versionCode != null) {
                saveFile.save.versionCode < VERSIONCODE
            } else false
            LoadSaveDialog(
                saveName = saveFile.name,
                load = {
                    loadSave(saveFile)
                    dialogState = SaveDialogState.None
                },
                cancel = {
                    dialogState = SaveDialogState.None
                },
                loadWithMigration = if (canMigrate) {
                    {
                        loadWithMigration(saveFile)
                        dialogState = SaveDialogState.None
                    }
                } else null
            )
        }

        is SaveDialogState.Delete -> {
            val saveFile = state.saveFile
            DeleteSaveDialog(
                saveName = saveFile.name,
                delete = {
                    deleteSave(saveFile)
                    dialogState = SaveDialogState.None
                },
                cancel = {
                    dialogState = SaveDialogState.None
                }
            )
        }

        is SaveDialogState.Overwrite -> {
            val saveFile = state.saveFile
            OverwriteSaveDialog(
                saveName = saveFile.name,
                overwrite = {
                    overwriteSave(saveFile)
                    dialogState = SaveDialogState.None
                },
                cancel = {
                    dialogState = SaveDialogState.None
                }
            )
        }

        SaveDialogState.Create -> {
            CreateSaveDialog(
                exists = { name ->
                    saveFiles.map { it.name.uppercase() }.contains(name.uppercase())
                },
                create = {
                    overwriteSave(SaveFile(it, Save.default)) /*Save data is populated later*/
                    dialogState = SaveDialogState.None
                },
                cancel = { dialogState = SaveDialogState.None },
            )
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBar(import = { importSave() }, create = { dialogState = SaveDialogState.Create }, navigateWithFade)
        },
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
    ) { innerPadding ->
        MainContent(
            saveFiles, exportSave,
            deleteSave = {
                dialogState = SaveDialogState.Delete(it)
            },
            loadSave = {
                dialogState = SaveDialogState.Load(it)
            },
            overwriteSave = {
                dialogState = SaveDialogState.Overwrite(it)
            }, innerPadding
        )
    }
}