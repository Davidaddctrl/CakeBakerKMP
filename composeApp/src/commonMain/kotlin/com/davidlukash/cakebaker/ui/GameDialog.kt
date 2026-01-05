package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.viewmodel.ViewModelProvided
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun GameDialog(
    theme: Theme,
    modifier: Modifier = Modifier,
    title: @Composable ColumnScope.() -> Unit,
    buttons: @Composable FlowRowScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    if (!ViewModelProvided.current)
        Box {
            GameDialogContent(theme, modifier, title, buttons, content)
        }
    else {
        val uiViewModel = LocalMainViewModel.current.uiViewModel
        val trueDensity by uiViewModel.trueDensity.collectAsState()
        CompositionLocalProvider(
            LocalDensity provides (trueDensity ?: LocalDensity.current),
        ) {
            Popup(
                alignment = Alignment.Center,
                onDismissRequest = null,
                properties = PopupProperties(
                    focusable = true,
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().background(
                        Color(0, 0, 0, 100)
                    )
                ) {
                    GameDialogContent(theme, modifier, title, buttons, content)
                }
            }
        }
    }
}

@Composable
fun BoxScope.GameDialogContent(
    theme: Theme,
    modifier: Modifier = Modifier,
    title: @Composable ColumnScope.() -> Unit,
    buttons: @Composable FlowRowScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    SmallContainer(
        theme = theme,
        modifier = modifier.align(Alignment.Center),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides theme.subtitleStyle.copy(
                    textAlign = TextAlign.Center,
                )
            ) {
                title()
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CompositionLocalProvider(
                    LocalTextStyle provides theme.smallLabelStyle.copy(
                        textAlign = TextAlign.Center,
                    )
                ) {
                    content()
                }
            }
            CompositionLocalProvider(
                LocalTextStyle provides theme.smallLabelStyle
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    buttons()
                }
            }
        }
    }
}


@Composable
@Preview(
    widthDp = 512, showBackground = true
)
fun GameDialogPreview() {
    val theme = getDefaultTheme()
    Box {
        GameDialogContent(
            theme = theme,
            title = {
                Text("Are you sure?")
            },
            buttons = {
                SmallThemedButton(
                    theme = theme,
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Overwrite")
                }
                SmallThemedButton(
                    theme = theme,
                    onClick = {},
                    modifier = Modifier
                ) {
                    Text("Import under different name")
                }
                SmallThemedButton(
                    theme = theme,
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
            },
        ) {
            Text("A save file with the same name already exists. How would you like to proceed?")
        }
    }
}