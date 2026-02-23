package com.davidlukash.cakebaker

import androidx.compose.ui.text.font.Font
import java.io.File

actual fun loadBytesToFont(identity: String, byteArray: ByteArray): Font {
    val file = File.createTempFile(identity, ".ttf")
    file.writeBytes(byteArray)
    val font = Font(file)
    return font
}