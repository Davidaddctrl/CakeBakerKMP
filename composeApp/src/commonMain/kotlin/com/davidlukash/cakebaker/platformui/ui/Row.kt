package com.davidlukash.cakebaker.platformui.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.davidlukash.cakebaker.platformui.HorizontalArrangement
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.RowScope

@Composable
expect fun Row(
    modifier: Modifier = Modifier,
    horizontalArrangement: HorizontalArrangement = HorizontalArrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable RowScope.() -> Unit,
)