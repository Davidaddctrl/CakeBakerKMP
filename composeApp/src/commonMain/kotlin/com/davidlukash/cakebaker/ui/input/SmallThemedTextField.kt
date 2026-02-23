package com.davidlukash.cakebaker.ui.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidlukash.cakebaker.data.theme.LocalDoDropShadow
import com.davidlukash.cakebaker.data.theme.LocalIsScaled
import com.davidlukash.cakebaker.data.theme.Theme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SmallThemedTextField(modifier: Modifier = Modifier, placeholder: String, value: String, singleLine: Boolean = true, setValue: (String) -> Unit) {
    CompositionLocalProvider(
        LocalIsScaled provides false,
        LocalDoDropShadow provides Theme.TextFieldTheme.shouldDropShadow
    ) {
        BasicTextField(
            value,
            onValueChange = { setValue(it) },
            modifier = modifier,
            cursorBrush = Theme.TextFieldTheme.cursorBrushDescriptor.toBrush(),
            textStyle = Theme.Styles.largeBodyStyle.copy(color = Theme.TextFieldTheme.contentColor),
            singleLine = singleLine,
            decorationBox = { innerTextField ->
                Surface(
                    modifier = Modifier,
                    color = Theme.TextFieldTheme.containerColor,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(8.dp, Theme.TextFieldTheme.borderColor)
                ) {
                    Box(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        if (value.isEmpty())
                            Text(
                                placeholder,
                                style = Theme.Styles.largeBodyStyle,
                                color = Theme.TextFieldTheme.placeholderColor
                            )
                        innerTextField()
                    }
                }
            }
        )
    }
}

@Preview(
    widthDp = 512,
)
@Composable
fun ThemedFieldPreview() {
    var value by remember { mutableStateOf("") }
    SmallThemedTextField(
        modifier = Modifier.width(256.dp),
        placeholder = "Save Name",
        value = value,
    ) { value = it }
}
