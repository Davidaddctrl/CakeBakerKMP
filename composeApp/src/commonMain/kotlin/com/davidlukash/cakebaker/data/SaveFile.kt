package com.davidlukash.cakebaker.data

import kotlinx.datetime.LocalDateTime

data class SaveFile(
    val name: String,
    val saveFileType: SaveFileType,
    val save: Save
)