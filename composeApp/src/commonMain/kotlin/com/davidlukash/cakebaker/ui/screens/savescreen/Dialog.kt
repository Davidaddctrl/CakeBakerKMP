package com.davidlukash.cakebaker.ui.screens.savescreen

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
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
import com.davidlukash.cakebaker.ui.GameDialog
import com.davidlukash.cakebaker.ui.input.SmallThemedButton
import com.davidlukash.cakebaker.ui.input.SmallThemedTextField
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LoadSaveDialog(
    saveName: String,
    load: () -> Unit,
    cancel: () -> Unit,
    loadWithMigration: (() -> Unit)? = null
) {
    GameDialog(
        modifier = Modifier.width(512.dp), title = { Text("Load Save?") },
        buttons = {
            SmallThemedButton(onClick = { load() }, modifier = Modifier.weight(1f), content = { Text("Load") })
            SmallThemedButton(onClick = { cancel() }, modifier = Modifier.weight(1f), content = { Text("Cancel") })
        },
        content = { Text("Loading save \"$saveName\" will overwrite your current progress." +
                if (loadWithMigration != null) " There are recommended migrations you may choose to apply." else "") },
    )
}

@Preview
@Composable
fun LoadSaveDialogPreview() {
    LoadSaveDialog(saveName = "default", load = {}, cancel = {})
}

@Preview
@Composable
fun LoadSaveDialogWithMigrationPreview() {
    LoadSaveDialog(saveName = "default", load = {}, cancel = {}) {}
}

@Composable
fun DeleteSaveDialog(saveName: String, delete: () -> Unit, cancel: () -> Unit) {
    GameDialog(
        modifier = Modifier.width(512.dp), title = { Text("Delete Save?") },
        buttons = {
            SmallThemedButton(onClick = { delete() }, modifier = Modifier.weight(1f), content = { Text("Delete") })
            SmallThemedButton(onClick = { cancel() }, modifier = Modifier.weight(1f), content = { Text("Cancel") })
        },
        content = { Text("Are you sure you want to delete save \"$saveName\" permanently?") },
    )
}

@Preview
@Composable
fun DeleteSaveDialogPreview() {
    DeleteSaveDialog(saveName = "default", delete = {}) {}
}

@Composable
fun OverwriteSaveDialog(saveName: String, overwrite: () -> Unit, cancel: () -> Unit) {
    GameDialog(
        modifier = Modifier.width(512.dp), title = { Text("Overwrite Save?") },
        buttons = {
            SmallThemedButton(
                onClick = { overwrite() },
                modifier = Modifier.weight(1f),
                content = { Text("Overwrite") })
            SmallThemedButton(onClick = { cancel() }, modifier = Modifier.weight(1f), content = { Text("Cancel") })
        },
        content = { Text("Are you sure you want to overwrite save \"$saveName\"? You cannot revert this change") },
    )
}

@Preview
@Composable
fun OverwriteSaveDialogPreview() {
    OverwriteSaveDialog(saveName = "save.json", overwrite = {}) {}
}

@Composable
fun CreateSaveDialog(
    exists: (String) -> Boolean,
    create: (String) -> Unit,
    cancel: () -> Unit,
    isImport: Boolean = false
) {
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
        modifier = Modifier.width(512.dp),
        offset = IntOffset(0, position.toInt()),
        title = { Text(if (isImport) "Import Save" else "Create Save") },
        buttons = {
            SmallThemedButton(
                onClick = { create(saveName) },
                modifier = Modifier.weight(1f),
                enabled = canCreate,
            ) { Text(if (isImport) "Import" else "Create") }
            SmallThemedButton(onClick = { cancel() }, modifier = Modifier.weight(1f), content = { Text("Cancel") })
        },
        {
            Text(
                "Save Name",
                textAlign = TextAlign.Start,
                modifier = Modifier.align(
                    Alignment.Start
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            SmallThemedTextField(
                modifier = Modifier.width(512.dp).onGloballyPositioned {
                    if (imeBottom == 0)
                        bottomToBottomHeight = (it.positionInWindow().y - it.size.height).toInt()
                },
                placeholder = "Save Name",
                value = saveName,
            ) { saveName = it }
            if (saveName.isNotEmpty() && !nameValid) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Save Name must" + (if (isBlank) " not be blank" else "") + (if (containsNonAlphanumeric && isBlank) " and must" else "") +
                            (if (containsNonAlphanumeric) " only contain lowercase alphanumeric characters" else ""),
                    style = Theme.Styles.smallBodyStyle,
                    textAlign = TextAlign.Start,
                    color = Theme.DangerColor,
                    modifier = Modifier.align(
                        Alignment.Start
                    )
                )
            }
            if (nameValid && alreadyExists) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "A save with this name already exists",
                    style = Theme.Styles.smallBodyStyle,
                    textAlign = TextAlign.Start,
                    color = Theme.DangerColor,
                    modifier = Modifier.align(
                        Alignment.Start
                    )
                )
            }
        }
    )
}

@Preview
@Composable
fun CreateSaveDialogPreview() {
    CreateSaveDialog(exists = { true }, create = {}, cancel = {})
}

@Preview
@Composable
fun ImportSaveDialogPreview() {
    CreateSaveDialog(exists = { true }, create = {}, cancel = {}, isImport = true)
}