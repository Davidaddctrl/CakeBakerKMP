package com.davidlukash.cakebaker

import androidx.compose.ui.text.font.Font
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import java.io.File

actual fun loadBytesToFont(identity: String, byteArray: ByteArray): Font {
    val file = File.createTempFile(identity, ".ttf")
    file.writeBytes(byteArray)
    val font = Font(file)
    return font
}

actual val client: HttpClient = HttpClient(CIO)
