package com.davidlukash.cakebaker.platformui.ui

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.davidlukash.cakebaker.platformui.Modifier

@Composable
expect fun Text(
    text: String,
    style: TextStyle = LocalTextStyle.current,
    modifier: Modifier = Modifier
)