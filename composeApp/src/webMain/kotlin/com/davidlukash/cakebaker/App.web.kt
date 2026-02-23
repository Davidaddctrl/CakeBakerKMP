package com.davidlukash.cakebaker

import androidx.compose.ui.text.font.Font

actual fun loadBytesToFont(identity: String, byteArray: ByteArray): Font = androidx.compose.ui.text.platform.Font(identity, byteArray)