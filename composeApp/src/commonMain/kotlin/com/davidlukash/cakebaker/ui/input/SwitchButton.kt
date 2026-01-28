package com.davidlukash.cakebaker.ui.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
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
    Surface(
        color = if (enabled) Theme.SwitchButtonTheme.containerColor else Theme.SwitchButtonTheme.disabledContainerColor,
        border = BorderStroke(8.dp, if (enabled) Theme.SwitchButtonTheme.borderColor else Theme.SwitchButtonTheme.disabledBorderColor),
        modifier = Modifier.clickable(onClick = { onClick(!value) }, enabled = enabled),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(modifier = modifier.height(48.dp)) {
            Box(modifier = Modifier.weight(1f)) {
                Surface(
                    color = if (!value && enabled)
                        Theme.SwitchButtonTheme.offSelectedContainerColor
                    else
                        Theme.SwitchButtonTheme.offUnselectedContainerColor,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            offText,
                            style = Theme.Styles.verySmallBodyStyle,
                            color = if (!value && enabled)
                                Theme.SwitchButtonTheme.offSelectedTextColor
                            else
                                Theme.SwitchButtonTheme.offUnselectedTextColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                Surface(
                    color = if (value && enabled)
                        Theme.SwitchButtonTheme.onSelectedContainerColor
                    else
                        Theme.SwitchButtonTheme.onUnselectedContainerColor,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            onText,
                            style = Theme.Styles.verySmallBodyStyle,
                            color = if (value && enabled)
                                Theme.SwitchButtonTheme.onSelectedTextColor
                            else
                                Theme.SwitchButtonTheme.onUnselectedTextColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SwitchButtonPreview() {
    var checked by remember { mutableStateOf(true) }
    SwitchButton(
        value = checked,
        onText = "On",
        offText = "Off",
    ) {
        checked = it
    }
}