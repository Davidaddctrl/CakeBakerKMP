package com.davidlukash.cakebaker.platformui.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.davidlukash.cakebaker.platformui.ColumnScope
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.VerticalArrangement

@Composable
expect fun Column(
    modifier: Modifier = Modifier,
    verticalArrangement: VerticalArrangement = VerticalArrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
)