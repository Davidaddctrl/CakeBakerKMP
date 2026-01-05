package com.davidlukash.cakebaker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.Theme
import com.davidlukash.cakebaker.data.theme.getDefaultTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ThemedField(theme: Theme, modifier: Modifier = Modifier, value: String, setValue: (String) -> Unit) {
    BasicTextField(
        value,
        onValueChange = { setValue(it) },
        modifier = modifier,
        cursorBrush = SolidColor(Color.White),
        textStyle = theme.buttonTextStyle.copy(color = Color.White),
        decorationBox = { innerTextField ->
            Surface(
                modifier = Modifier,
                color = theme.buttonTheme.disabledBorderColor,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(8.dp, theme.buttonTheme.disabledContainerColor)
            ) {
                Box(
                    modifier = Modifier.padding(16.dp)
                ) {
                    innerTextField()
                }
            }
        }
    )
}

@Preview(
    widthDp = 512,
)
@Composable
fun ThemedFieldPreview() {
    val theme = getDefaultTheme()
    var value by remember { mutableStateOf("Value") }
    ThemedField(
        theme = theme,
        modifier = Modifier.width(256.dp),
        value = value,
        setValue = { value = it },
    )
}
