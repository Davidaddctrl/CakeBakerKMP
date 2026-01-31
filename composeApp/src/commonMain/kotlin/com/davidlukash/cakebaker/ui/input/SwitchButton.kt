package com.davidlukash.cakebaker.ui.input

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.platformui.Modifier
import com.davidlukash.cakebaker.platformui.ui.Box
import com.davidlukash.cakebaker.platformui.ui.Row
import com.davidlukash.cakebaker.platformui.ui.Text
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun SwitchButton(
    value: Boolean,
    onText: String,
    offText: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: (Boolean) -> Unit
) {
    TransparentButton(
        onClick = { onClick(!value) },
        padding = 0.dp
    ) {
        Row(
            modifier = modifier.height(48.dp)
                .background(
                    if (enabled) Theme.SwitchButtonTheme.containerColor
                    else Theme.SwitchButtonTheme.disabledContainerColor,
                    4.dp
                )
                .border(
                    color = if (enabled) Theme.SwitchButtonTheme.borderColor else Theme.SwitchButtonTheme.disabledBorderColor,
                    width = 8.dp,
                    borderRadius = 4.dp
                )

        ) {
            Box(
                modifier =
                    Modifier.weight(1f).fillMaxHeight().background(
                        if (!value && enabled)
                            Theme.SwitchButtonTheme.offSelectedContainerColor
                        else
                            Theme.SwitchButtonTheme.offUnselectedContainerColor
                    )
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides (if (!value && enabled)
                        Theme.SwitchButtonTheme.offSelectedTextColor
                    else
                        Theme.SwitchButtonTheme.offUnselectedTextColor)
                ) {
                    Text(
                        offText,
                        style = Theme.Styles.verySmallBodyStyle.copy(textAlign = TextAlign.Center),
                        modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                    )
                }
            }
            Box(
                modifier = Modifier.weight(1f).fillMaxHeight().background(
                    if (value && enabled)
                        Theme.SwitchButtonTheme.onSelectedContainerColor
                    else
                        Theme.SwitchButtonTheme.onUnselectedContainerColor
                )
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides (if (value && enabled)
                        Theme.SwitchButtonTheme.onSelectedTextColor
                    else
                        Theme.SwitchButtonTheme.onUnselectedTextColor)
                ) {
                    Text(
                        onText,
                        style = Theme.Styles.verySmallBodyStyle.copy(textAlign = TextAlign.Center),
                        modifier = Modifier.fillMaxWidth().align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun SwitchButton(
    value: Boolean,
    onText: String,
    offText: String,
    enabled: Boolean = true,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    onClick: (Boolean) -> Unit
) {
    SwitchButton(
        value = value,
        onText = onText,
        offText = offText,
        enabled = enabled,
        modifier = Modifier.nativeComposeModifier(modifier),
        onClick = onClick
    )
}

@Preview
@Composable
fun SwitchButtonPreview() {
    var checked by remember { mutableStateOf(true) }
    SwitchButton(
        value = checked,
        onText = "On",
        offText = "Off",
        modifier = Modifier
    ) {
        checked = it
    }
}