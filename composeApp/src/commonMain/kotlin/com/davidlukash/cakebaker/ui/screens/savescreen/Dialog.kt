package com.davidlukash.cakebaker.ui.screens.savescreen

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import com.davidlukash.cakebaker.ui.GameDialog
import com.davidlukash.cakebaker.ui.SmallThemedButton
import com.davidlukash.cakebaker.ui.ThemedField
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LoadSaveDialog(theme: Theme, saveName: String, load: () -> Unit, cancel: () -> Unit) {
    GameDialog(
        theme = theme, modifier = Modifier.width(512.dp), title = { Text("Load Save?") },
        buttons = {
            SmallThemedButton(theme = theme, modifier = Modifier.weight(1f), onClick = { load() }) { Text("Load") }
            SmallThemedButton(theme = theme, modifier = Modifier.weight(1f), onClick = { cancel() }) { Text("Cancel") }
        },
    ) { Text("Loading save \"$saveName\" will overwrite your current progress.") }
}

@Preview
@Composable
fun LoadSaveDialogPreview() {
    LoadSaveDialog(theme = getDefaultTheme(), saveName = "default", load = {}, cancel = {})
}

@Composable
fun DeleteSaveDialog(theme: Theme, saveName: String, delete: () -> Unit, cancel: () -> Unit) {
    GameDialog(
        theme = theme, modifier = Modifier.width(512.dp), title = { Text("Delete Save?") },
        buttons = {
            SmallThemedButton(theme = theme, modifier = Modifier.weight(1f), onClick = { delete() }) { Text("Delete") }
            SmallThemedButton(theme = theme, modifier = Modifier.weight(1f), onClick = { cancel() }) { Text("Cancel") }
        },
    ) { Text("Are you sure you want to delete save \"$saveName\" permanently?") }
}

@Preview
@Composable
fun DeleteSaveDialogPreview() {
    DeleteSaveDialog(theme = getDefaultTheme(), saveName = "default", delete = {}, cancel = {})
}

@Composable
fun OverwriteSaveDialog(theme: Theme, saveName: String, overwrite: () -> Unit, cancel: () -> Unit) {
    GameDialog(
        theme = theme, modifier = Modifier.width(512.dp), title = { Text("Overwrite Save?") },
        buttons = {
            SmallThemedButton(
                theme = theme,
                modifier = Modifier.weight(1f),
                onClick = { overwrite() }) { Text("Overwrite") }
            SmallThemedButton(theme = theme, modifier = Modifier.weight(1f), onClick = { cancel() }) { Text("Cancel") }
        },
    ) { Text("Are you sure you want to overwrite save \"$saveName\"? You cannot revert this change") }
}

@Preview
@Composable
fun OverwriteSaveDialogPreview() {
    OverwriteSaveDialog(theme = getDefaultTheme(), saveName = "save.json", overwrite = {}, cancel = {})
}

@Composable
fun CreateSaveDialog(theme: Theme, exists: (String) -> Boolean,create: (String) -> Unit, cancel: () -> Unit, isImport: Boolean = false) {
    var saveName by remember { mutableStateOf("") }
    val containsNonAlphanumeric = saveName.contains(Regex("[^a-z0-9]"))
    val isBlank = saveName.isBlank()
    val nameValid = !isBlank && !containsNonAlphanumeric
    val alreadyExists = exists(saveName)
    val canCreate = nameValid && !alreadyExists

    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
    var bottomToBottomHeight by remember { mutableStateOf(0) }
    val position by animateFloatAsState(
        targetValue = (if (imeBottom > 0) bottomToBottomHeight - imeBottom else 0).toFloat(),
        animationSpec = tween(durationMillis = 200, easing = LinearOutSlowInEasing)
    )

    //This is a popup internally
    GameDialog(
        offset = IntOffset(0, position.toInt()), theme = theme, modifier = Modifier.width(512.dp), title = { Text(if (isImport) "Import Save" else "Create Save") },
        buttons = {
            SmallThemedButton(
                theme = theme,
                modifier = Modifier.weight(1f),
                onClick = { create(saveName) },
                enabled = canCreate
            ) { Text(if (isImport) "Import" else "Create") }
            SmallThemedButton(theme = theme, modifier = Modifier.weight(1f), onClick = { cancel() }) { Text("Cancel") }
        }
    ) {
        Text(
            "Save Name", style = theme.smallLabelStyle, textAlign = TextAlign.Start, modifier = Modifier.align(
                Alignment.Start
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        ThemedField(
            theme = theme,
            value = saveName,
            placeholder = "Save Name",
            modifier = Modifier.width(512.dp).onGloballyPositioned {
                if (imeBottom == 0)
                    bottomToBottomHeight = (it.positionInWindow().y - it.size.height).toInt()
            },
            setValue = { saveName = it },
        )
        if (saveName.isNotEmpty() && !nameValid) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Save Name must" + (if (isBlank) " not be blank" else "") + (if (containsNonAlphanumeric && isBlank) " and must" else "") +
                        (if (containsNonAlphanumeric) " only contain lowercase alphanumeric characters" else ""),
                style = theme.extremelySmallLabelStyle,
                textAlign = TextAlign.Start,
                color = theme.red,
                modifier = Modifier.align(
                    Alignment.Start
                )
            )
        }
        if (nameValid && alreadyExists) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "A save with this name already exists",
                style = theme.extremelySmallLabelStyle,
                textAlign = TextAlign.Start,
                color = theme.red,
                modifier = Modifier.align(
                    Alignment.Start
                )
            )
        }
    }
}

@Preview
@Composable
fun CreateSaveDialogPreview() {
    CreateSaveDialog(theme = getDefaultTheme(), exists = { true }, create = {}, cancel = {})
}