package com.davidlukash.cakebaker.ui.screens.savescreen

import com.davidlukash.cakebaker.data.save.SaveFile

sealed interface SaveDialogState {
    data object None : SaveDialogState
    data class Load(val saveFile: SaveFile): SaveDialogState
    data class Delete(val saveFile: SaveFile): SaveDialogState
    data class Overwrite(val saveFile: SaveFile): SaveDialogState
    data object Create: SaveDialogState
}