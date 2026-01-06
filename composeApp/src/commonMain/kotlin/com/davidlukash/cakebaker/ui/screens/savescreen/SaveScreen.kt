package com.davidlukash.cakebaker.ui.screens.savescreen

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.davidlukash.cakebaker.data.Save
import com.davidlukash.cakebaker.data.SaveFile
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.navigation.Screen

@Composable
fun SaveScreen(
    theme: Theme,
    saveFiles: List<SaveFile>,
    navigateWithFade: (Screen) -> Unit,
    exportSave: (SaveFile) -> Unit,
    deleteSave: (SaveFile) -> Unit,
    loadSave: (SaveFile) -> Unit,
    overwriteSave: (SaveFile) -> Unit,
    importSave: () -> Unit
) {
    var dialogType by remember { mutableStateOf(SaveDialogType.NONE) }
    var dialogData by remember { mutableStateOf<SaveFile?>(null) }
    when (dialogType) {
        SaveDialogType.NONE -> {}
        SaveDialogType.LOAD -> {
            LoadSaveDialog(
                theme = theme,
                saveName = dialogData?.name!!,
                load = {
                    dialogType = SaveDialogType.NONE
                    loadSave(dialogData!!)
                    dialogData = null
                },
                cancel = {
                    dialogType = SaveDialogType.NONE
                    dialogData = null
                },
            )
        }

        SaveDialogType.DELETE -> {
            DeleteSaveDialog(
                theme = theme,
                saveName = dialogData?.name!!,
                delete = {
                    dialogType = SaveDialogType.NONE
                    deleteSave(dialogData!!)
                    dialogData = null
                },
                cancel = {
                    dialogType = SaveDialogType.NONE
                    dialogData = null
                },
            )
        }

        SaveDialogType.OVERWRITE -> {
            OverwriteSaveDialog(
                theme = theme,
                saveName = dialogData?.name!!,
                overwrite = {
                    dialogType = SaveDialogType.NONE
                    overwriteSave(dialogData!!)
                    dialogData = null
                },
                cancel = {
                    dialogType = SaveDialogType.NONE
                    dialogData = null
                },
            )
        }

        SaveDialogType.CREATE -> {
            CreateSaveDialog(
                theme = theme,
                exists = { name ->
                    saveFiles.map { it.name.uppercase() }.contains(name.uppercase())
                },
                create = {
                    overwriteSave(SaveFile(it, Save.default)) /*Save data is populated later*/
                    dialogType = SaveDialogType.NONE
                },
                cancel = { dialogType = SaveDialogType.NONE },
            )
        }
    }
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopBar(theme)
        },
        bottomBar = {
            BottomBar(theme, import = { importSave() }, create = { dialogType = SaveDialogType.CREATE }, navigateWithFade)
        }
    ) { innerPadding ->
        MainContent(
            theme, saveFiles, exportSave,
            deleteSave = {
                dialogType = SaveDialogType.DELETE
                dialogData = it
            },
            loadSave = {
                dialogType = SaveDialogType.LOAD
                dialogData = it
            },
            overwriteSave = {
                dialogType = SaveDialogType.OVERWRITE
                dialogData = it
            }, innerPadding
        )
    }
}