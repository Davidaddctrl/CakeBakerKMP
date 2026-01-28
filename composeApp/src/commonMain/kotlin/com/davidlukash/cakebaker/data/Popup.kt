package com.davidlukash.cakebaker.data

import androidx.compose.runtime.Composable
import com.davidlukash.cakebaker.data.theme.Theme
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Popup @OptIn(ExperimentalUuidApi::class) constructor(
    val content: @Composable Popup.() -> Unit,
    val shouldHaveDefaultButton: Boolean = true,
    val uuid: Uuid = Uuid.random(),
)