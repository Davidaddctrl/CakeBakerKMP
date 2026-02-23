package com.davidlukash.cakebaker

import androidx.compose.ui.text.font.Font
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

actual fun loadBytesToFont(identity: String, byteArray: ByteArray): Font = androidx.compose.ui.text.platform.Font(identity, byteArray)
actual val client: HttpClient = HttpClient(CIO)