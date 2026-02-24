package com.davidlukash.cakebaker.ui.screens.themescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import cakebaker.composeapp.generated.resources.Res
import cakebaker.composeapp.generated.resources.chevron_backward
import cakebaker.composeapp.generated.resources.chevron_forward
import cakebaker.composeapp.generated.resources.delete
import cakebaker.composeapp.generated.resources.download
import com.davidlukash.cakebaker.data.theme.Theme
import org.jetbrains.compose.resources.vectorResource

fun <T> SnapshotStateList<T>.swap(oldIndex: Int, newIndex: Int) {
    this[oldIndex] = this[newIndex].also { this[newIndex] = this[oldIndex] }
}

@Composable
fun MainContent(
    themes: List<String>,
    exportTheme: (String) -> Unit,
    deleteTheme: (String) -> Unit,
    innerPadding: PaddingValues,
    selectedThemes: SnapshotStateList<String>,
) {
    val availableThemes = (themes.toSet() - selectedThemes.toSet()).toList()
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(innerPadding).padding(vertical = 16.dp),
    ) {
        ThemeItemColumn(
            text = Theme.getString("title.available")
        ) {
            items(availableThemes.size, key = { availableThemes[it] }) { index ->
                val theme = availableThemes[index]
                ThemeItem(
                    theme = theme,
                    after = {
                        Spacer(Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                exportTheme(theme)
                            },
                            modifier = Modifier.size(64.dp)
                        ) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.download),
                                modifier = Modifier.size(48.dp),
                                contentDescription = Theme.getString("content_description.export_theme"),
                            )
                        }
                        IconButton(
                            onClick = {
                                deleteTheme(theme)
                            },
                            modifier = Modifier.size(64.dp)
                        ) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.delete),
                                modifier = Modifier.size(48.dp),
                                contentDescription = Theme.getString("content_description.delete_theme"),
                            )
                        }
                        IconButton(
                            onClick = {
                                selectedThemes.add(theme)
                            },
                            modifier = Modifier.size(64.dp)
                        ) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.chevron_forward),
                                modifier = Modifier.size(64.dp),
                                contentDescription = Theme.getString("content_description.select_theme"),
                            )
                        }
                    }
                )
            }
        }
        ThemeItemColumn(
            text = Theme.getString("title.selected")
        ) {
            item("default") {
                ThemeItem(
                    theme = "default",
                    before = {
                        IconButton(
                            onClick = {
                                exportTheme("default")
                            },
                            modifier = Modifier.size(64.dp)
                        ) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.download),
                                modifier = Modifier.size(48.dp),
                                contentDescription = Theme.getString("content_description.export_theme"),
                            )
                        }
                        Spacer(Modifier.weight(1f))
                    },
                )
            }
            items(selectedThemes.size, key = { selectedThemes[it] }) { index ->
                val theme = selectedThemes[index]
                ThemeItem(
                    theme = theme,
                    before = {
                        IconButton(
                            onClick = {
                                selectedThemes.remove(theme)
                            },
                            modifier = Modifier.size(64.dp)
                        ) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.chevron_backward),
                                modifier = Modifier.size(64.dp),
                                contentDescription = Theme.getString("content_description.deselect_theme"),
                            )
                        }
                        IconButton(
                            onClick = {
                                selectedThemes.swap(index, index - 1)
                            },
                            modifier = Modifier.size(64.dp),
                            enabled = index != 0
                        ) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.chevron_forward),
                                modifier = Modifier.size(64.dp).rotate(-90f),
                                contentDescription = Theme.getString("content_description.move_up"),
                            )
                        }
                        IconButton(
                            onClick = {
                                selectedThemes.swap(index, index + 1)
                            },
                            modifier = Modifier.size(64.dp),
                            enabled = index != selectedThemes.size - 1
                        ) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.chevron_backward),
                                modifier = Modifier.size(64.dp).rotate(-90f),
                                contentDescription = Theme.getString("content_description.move_down"),
                            )
                        }
                        Spacer(Modifier.weight(1f))
                    }
                )
            }
        }
    }
}