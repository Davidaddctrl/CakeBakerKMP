package com.davidlukash.cakebaker.ui.screens.themescreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.davidlukash.cakebaker.data.theme.Theme

@Composable
fun TopBar() {
    Text(
        Theme.getString("title.themes"),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        style = Theme.Styles.titleStyle,
    )
}