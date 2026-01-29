package com.davidlukash.cakebaker.platformui.ui

import androidx.compose.runtime.Composable
import com.davidlukash.cakebaker.platformui.BoxScope
import com.davidlukash.cakebaker.platformui.Modifier

@Composable
expect fun Box(
    modifier: Modifier,
    content: @Composable BoxScope.() -> Unit
)