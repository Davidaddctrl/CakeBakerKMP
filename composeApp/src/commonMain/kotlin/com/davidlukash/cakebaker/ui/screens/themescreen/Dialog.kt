package com.davidlukash.cakebaker.ui.screens.themescreen

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
fun DeleteThemeDialog(themeName: String, delete: () -> Unit, cancel: () -> Unit) {
    GameDialog(
        modifier = Modifier.width(512.dp), title = { Text(Theme.getString("dialog.delete_theme.title")) },
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
        content = { Text(Theme.getString("dialog.delete_theme.delete_text").replace("{0}", themeName)) },
    )
}

@Preview
@Composable
fun DeleteThemeDialogPreview() {
    DeleteThemeDialog(themeName = "default", delete = {}) {}
}

@Composable
fun ImportThemeDialog(
    exists: (String) -> Boolean,
    import: (String) -> Unit,
    cancel: () -> Unit,
) {
    var themeName by remember { mutableStateOf("") }
    val containsNonAlphanumeric = themeName.contains(Regex("[^a-z0-9]"))
    val isBlank = themeName.isBlank()
    val nameValid = !isBlank && !containsNonAlphanumeric
    val alreadyExists = exists(themeName)
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
        title = { Text(Theme.getString("dialog.import_theme.title")) },
        buttons = {
            SmallThemedButton(
                onClick = { import(themeName) },
                modifier = Modifier.weight(1f),
                enabled = canCreate,
            ) { Text(Theme.getString("action.import")) }
            SmallThemedButton(
                onClick = { cancel() },
                modifier = Modifier.weight(1f),
                content = { Text(Theme.getString("action.cancel")) })
        }
    ) {
        Text(
            Theme.getString("dialog.import_theme.theme_name_field.title"),
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
            placeholder = Theme.getString("dialog.import_theme.theme_name_field.title"),
            value = themeName,
        ) { themeName = it }
        if (themeName.isNotEmpty() && !nameValid) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                Theme.getString("dialog.import_theme.error.invalid_name"),
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
                Theme.getString("dialog.import_theme.error.already_exists"),
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
fun ImportThemeDialogPreview() {
    ImportThemeDialog(exists = { false }, import = {}, cancel = {})
}