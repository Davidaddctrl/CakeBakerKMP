package com.davidlukash.cakebaker.data.save

import kotlinx.serialization.Serializable

@Serializable
data class SaveFile(
    val name: String,
    val save: Save,
    val isDefault: Boolean = false
)