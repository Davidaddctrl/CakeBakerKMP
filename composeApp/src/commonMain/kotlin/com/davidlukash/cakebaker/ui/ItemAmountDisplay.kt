package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.Item
import com.davidlukash.cakebaker.toEngNotation
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel

@Composable
fun ItemAmountDisplay(item: Item) {
    val mainViewModel = LocalMainViewModel.current
    val themeViewModel = mainViewModel.themeViewModel
    val theme by themeViewModel.theme.collectAsState()
    val image = theme.nameToImage(item.name)
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.defaultMinSize(minHeight = 224.dp)
    ) {
        Text(
            item.name.replace(" ", "\n"),
            color = Color.White,
            textAlign = TextAlign.Center,
            style = if (item.name.contains(" ")) theme.smallLabelStyle else theme.labelStyle,
            fontFamily = LocalFontFamily.current
        )
        Column(
            modifier = Modifier.height(96.dp),
            verticalArrangement = Arrangement.Center
        ) {
            ResourceImage(
                image,
                modifier = Modifier.height(if (item.name == "Money") 48.dp else 96.dp)
            )
        }
        Text(
            "Amount",
            color = Color.White,
            textAlign = TextAlign.Center,
            style = theme.labelStyle,
            fontFamily = LocalFontFamily.current
        )
        Text(
            toEngNotation(item.amount),
            color = Color.White,
            textAlign = TextAlign.Center,
            style = theme.labelStyle,
            fontFamily = LocalFontFamily.current
        )
    }
}