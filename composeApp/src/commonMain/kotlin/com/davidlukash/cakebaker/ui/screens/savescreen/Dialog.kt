package com.davidlukash.cakebaker.ui.screens.savescreen

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.ui.GameDialog
import com.davidlukash.cakebaker.ui.input.SmallThemedButton
import com.davidlukash.cakebaker.ui.input.SmallThemedTextField
import com.davidlukash.cakebaker.ui.input.SwitchButton
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LoadSaveDialog(
    saveName: String,
    load: () -> Unit,
    cancel: () -> Unit,
    loadWithMigration: (() -> Unit)? = null
) {
    var shouldMigrate by remember { mutableStateOf(true) }
    GameDialog(
        modifier = Modifier.width(512.dp), title = { Text(Theme.getString("dialog.load_save.title")) },
        buttons = {
            SmallThemedButton(
                onClick = {
                    if (loadWithMigration == null || !shouldMigrate) {
                        load()
                        return@SmallThemedButton
                    }
                    loadWithMigration.invoke()
                },
                modifier = Modifier.weight(1f),
                content = { Text(Theme.getString("action.load")) })
            SmallThemedButton(
                onClick = { cancel() },
                modifier = Modifier.weight(1f),
                content = { Text(Theme.getString("action.cancel")) })
        },
        content = {
            Text(
                Theme.getString("dialog.load_save.load_text").replace("{0}", saveName) +
                        if (loadWithMigration != null) " " + Theme.getString("dialog.load_save.migration_text") else ""
            )
            if (loadWithMigration != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    Theme.getString("dialog.load_save.migration_title").replace("{0}", saveName),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    textDecoration = TextDecoration.Underline,
                )
                Spacer(Modifier.height(8.dp))
                SwitchButton(
                    value = shouldMigrate,
                    onText = Theme.getString("action.yes"),
                    offText = Theme.getString("action.no"),
                    modifier = Modifier.fillMaxWidth(),
                    height = 36.dp,
                    borderWidth = 6.dp,
                    textStyle = Theme.Styles.mediumBodyStyle
                ) { shouldMigrate = it }
            }
        },
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
        modifier = Modifier.width(512.dp), title = { Text(Theme.getString("dialog.delete_save.title")) },
        buttons = {
            SmallThemedButton(
                onClick = { delete() },
                modifier = Modifier.weight(1f),
                content = { Text(Theme.getString("action.delete")) })
            SmallThemedButton(
                onClick = { cancel() },
                modifier = Modifier.weight(1f),
                content = { Text(Theme.getString("action.cancel")) })
        },
        content = { Text(Theme.getString("dialog.delete_save.delete_text").replace("{0}", saveName)) },
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
        modifier = Modifier.width(512.dp), title = { Text(Theme.getString("dialog.overwrite_save.title")) },
        buttons = {
            SmallThemedButton(
                onClick = { overwrite() },
                modifier = Modifier.weight(1f),
                content = { Text(Theme.getString("action.overwrite")) })
            SmallThemedButton(
                onClick = { cancel() },
                modifier = Modifier.weight(1f),
                content = { Text(Theme.getString("action.cancel")) })
        },
        content = { Text(Theme.getString("dialog.overwrite_save.overwrite_text").replace("{0}", saveName)) },
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
        title = { Text(if (isImport) Theme.getString("dialog.create_save.import_title") else Theme.getString("dialog.create_save.create_title")) },
        buttons = {
            SmallThemedButton(
                onClick = { create(saveName) },
                modifier = Modifier.weight(1f),
                enabled = canCreate,
            ) { Text(if (isImport) Theme.getString("action.import") else Theme.getString("action.create")) }
            SmallThemedButton(onClick = { cancel() }, modifier = Modifier.weight(1f), content = { Text("Cancel") })
        }
    ) {
        Text(
            Theme.getString("dialog.create_save.save_name_field.title"),
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
            placeholder = Theme.getString("dialog.create_save.save_name_field.title"),
            value = saveName,
        ) { saveName = it }
        if (saveName.isNotEmpty() && !nameValid) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                Theme.getString("dialog.create_save.error.invalid_name"),
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
                Theme.getString("dialog.create_save.error.already_exists"),
                style = Theme.Styles.smallBodyStyle,
                textAlign = TextAlign.Start,
                color = Theme.DangerColor,
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
    CreateSaveDialog(exists = { true }, create = {}, cancel = {})
}

@Preview
@Composable
fun ImportSaveDialogPreview() {
    CreateSaveDialog(exists = { true }, create = {}, cancel = {}, isImport = true)
}