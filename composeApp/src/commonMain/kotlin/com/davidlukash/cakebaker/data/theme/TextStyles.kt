package com.davidlukash.cakebaker.data.theme

import androidx.compose.ui.text.TextStyle
import com.davidlukash.cakebaker.data.theme.json.JsonTextStyles

data class TextStyles(
    val titleStyle: TextStyle,
    val buttonTextStyle: TextStyle,
    val largeBodyStyle: TextStyle,
    val mediumBodyStyle: TextStyle,
    val smallBodyStyle: TextStyle,
    val verySmallBodyStyle: TextStyle,
) {
    fun toJsonTheme() = JsonTextStyles(
        titleStyle = titleStyle,
        buttonTextStyle = buttonTextStyle,
        largeBodyStyle = largeBodyStyle,
        mediumBodyStyle = mediumBodyStyle,
        smallBodyStyle = smallBodyStyle,
        verySmallBodyStyle = verySmallBodyStyle,
    )
}
