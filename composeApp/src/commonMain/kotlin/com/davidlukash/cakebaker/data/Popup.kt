package com.davidlukash.cakebaker.data

import androidx.compose.runtime.Composable
import com.davidlukash.cakebaker.data.theme.Theme

data class Popup(
    val content: @Composable Pair<Popup, Theme>.() -> Unit,
    val id: Int,
    val shouldHaveDefaultButton: Boolean = true,
    //set later
    val index: Int = -1
)