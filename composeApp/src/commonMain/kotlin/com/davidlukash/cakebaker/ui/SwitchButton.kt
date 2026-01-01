package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.isDark
import com.davidlukash.cakebaker.isLight
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel

@Composable
fun SwitchButton(
    value: Boolean,
    onText: String,
    offText: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: (Boolean) -> Unit
) {
    val mainViewModel = LocalMainViewModel.current
    val themeViewModel = mainViewModel.themeViewModel
    val theme by themeViewModel.theme.collectAsState()
    Surface(
        color = theme.containerBorderColor,
        border = BorderStroke(8.dp, theme.containerBorderColor),
        modifier = Modifier.clickable(onClick = { onClick(!value) }, enabled = enabled),
    ) {
        Row(modifier = modifier.height(48.dp)) {
            Box(modifier = Modifier.weight(1f)) {
                Surface(
                    color = theme.red.copy(alpha = if (!value && enabled) 1f else 0.3f),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            offText,
                            fontFamily = LocalFontFamily.current,
                            style = theme.smallLabelStyle,
                            color = (if (theme.red.isDark) Color.White else Color.Black).copy(alpha = if (!value && enabled) 1f else 0.3f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                Surface(
                    color = theme.green.copy(alpha = if (value && enabled) 1f else 0.3f),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            onText,
                            fontFamily = LocalFontFamily.current,
                            style = theme.smallLabelStyle,
                            color = (if (theme.green.isLight) Color.Black else Color.White).copy(alpha = if (value && enabled) 1f else 0.3f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}