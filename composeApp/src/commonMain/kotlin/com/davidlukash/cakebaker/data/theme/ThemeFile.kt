package com.davidlukash.cakebaker.data.theme

import com.davidlukash.cakebaker.data.theme.json.JsonTheme
import kotlinx.serialization.Serializable

@Serializable
data class ThemeFile(
    val name: String,
    val theme: JsonTheme
)