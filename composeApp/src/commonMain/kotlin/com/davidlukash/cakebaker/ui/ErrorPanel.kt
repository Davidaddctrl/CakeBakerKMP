package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.horizontalRowScroll
import com.davidlukash.cakebaker.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun BasicErrorPanel(
    errors: List<Throwable>,
    modifier: Modifier = Modifier,
    hostState: SnackbarHostState = remember { SnackbarHostState() },
    buttons: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            Text(
                "An error has occurred that the game cannot recover from",
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                buttons()
            }
        },
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState) }
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize().horizontalScroll(scrollState)
                .horizontalRowScroll(coroutineScope, scrollState, doScrollWheel = false).padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(errors) { error ->
                SelectionContainer {
                    Text(error.stackTraceToString(), color = MaterialTheme.colorScheme.error)
                }
            }
            item {
                Box(modifier = Modifier.height(512.dp))
            }
        }
    }
}

@Composable
fun ErrorPanel(
    errors: List<Throwable>,
    viewModel: MainViewModel? = null,
    quitApp: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    addError: (Throwable) -> Unit,
) {
    val hostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    BasicErrorPanel(
        errors = errors,
        hostState = hostState,
        modifier = modifier,
    ) {
        quitApp?.let {
            Button(
                onClick = { quitApp() }
            ) {
                Text("Quit")
            }
        }
        viewModel?.let {
            Button(
                onClick = {
                    try {
                        viewModel.createCrashSave()
                        if (quitApp == null) {
                            coroutineScope.launch {
                                hostState.showSnackbar("Save Recovered")
                            }
                        } else {
                            quitApp()
                        }
                    } catch (e: Throwable) {
                        addError(e)
                    }
                },
            ) {
                Text("Attempt to Recover Save" + if (quitApp == null) "" else " and Quit")
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(widthDp = 1920, heightDp = 1080, showBackground = true)
@Composable
fun ErrorPanelPreview() {
    val error = Exception("Something went wrong")

    ErrorPanel(listOf(error), addError = {}, modifier = Modifier.fillMaxSize())
}