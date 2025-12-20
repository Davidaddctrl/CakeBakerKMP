package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel

@Composable
fun RowScope.DebugMenu() {
    val mainViewModel = LocalMainViewModel.current
    val dataViewModel = mainViewModel.dataViewModel
    val cakes by dataViewModel.cakesFlow.collectAsState(initial = emptyList())
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Debug Menu",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline
        )
        cakes.forEach { cake ->
            key(cake) {
                Button(
                    onClick = {
                        dataViewModel.debugSkipOrderTimer(cake.cakeTier ?: 1)
                    }
                ) {
                    Text("Skip timer for ${cake.name}")
                }
            }
        }
    }
}