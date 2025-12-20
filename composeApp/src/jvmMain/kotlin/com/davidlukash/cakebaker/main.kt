package com.davidlukash.cakebaker
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ionspin.kotlin.bignum.decimal.BigDecimal

fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Cake Baker",
        ) {
            App()
        }
    }
}