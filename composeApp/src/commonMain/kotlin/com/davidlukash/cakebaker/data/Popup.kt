package com.davidlukash.cakebaker.data

import androidx.compose.runtime.Composable

data class Popup(
    val content: @Composable Popup.() -> Unit,
    val id: Int,
    val shouldHaveDefaultButton: Boolean = true,
    //set later
    val index: Int = -1
)