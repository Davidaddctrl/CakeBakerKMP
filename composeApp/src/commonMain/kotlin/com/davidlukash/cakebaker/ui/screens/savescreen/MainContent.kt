package com.davidlukash.cakebaker.ui.screens.savescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.Save
import com.davidlukash.cakebaker.data.SaveFile
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel

@Composable
fun MainContent(
    theme: Theme,
    exportSave: (SaveFile) -> Unit,
    deleteSave: (SaveFile) -> Unit,
    loadSave: (SaveFile) -> Unit,
    overwriteSave: (SaveFile) -> Unit,
    innerPadding: PaddingValues
) {
    val mainViewModel = LocalMainViewModel.current
    val saveFileViewModel = mainViewModel.saveFileViewModel
    val saveFiles by saveFileViewModel.savesFlow.collectAsState(initial = emptyList())
    val default = remember { SaveFile("Default", Save.default, true) }
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize().padding(innerPadding).padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        columns = GridCells.Adaptive(760.dp)
    ) {
        item {
            SaveItem(theme, exportSave, deleteSave, loadSave, overwriteSave,default)
        }
        items(saveFiles.size) { index ->
            val saveFile = saveFiles[index]
            SaveItem(theme, exportSave, deleteSave, loadSave, overwriteSave, saveFile)
        }
    }
}