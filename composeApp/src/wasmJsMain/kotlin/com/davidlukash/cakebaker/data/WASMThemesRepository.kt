package com.davidlukash.cakebaker.data

import com.davidlukash.cakebaker.data.theme.ThemeFile
import com.davidlukash.cakebaker.data.theme.json.JsonTheme
import com.davidlukash.cakebaker.json
import com.davidlukash.cakebaker.mainViewModel
import com.davidlukash.cakebaker.repository.ThemesRepository
import io.ktor.util.toByteArray
import kotlinx.browser.document
import kotlinx.browser.window
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import org.w3c.files.FileReader
import org.w3c.files.get

class WASMThemesRepository : ThemesRepository() {
    private val localStorage = window.localStorage

    private fun updateThemes(list: List<ThemeFile>) {
        localStorage.setItem("themes", json.encodeToString(list))
    }

    override suspend fun listThemes(): List<ThemeFile> {
        val themesString = localStorage.getItem("themes") ?: "[]"
        val themes = json.decodeFromString<List<ThemeFile>>(themesString)
        return themes
    }

    override suspend fun deleteTheme(name: String): Boolean {
        val themes = listThemes()
        if (!themes.map { it.name }.contains(name)) return false
        updateThemes(
            themes.filterNot { it.name == name }
        )
        return true
    }

    override suspend fun upsertTheme(file: ThemeFile): Boolean {
        val themes = listThemes()
        val existsBefore = themes.map { it.name }.contains(file.name)
        updateThemes((listOf(file) + themes).distinctBy { it.name })
        return existsBefore
    }

    @OptIn(ExperimentalWasmJsInterop::class)
    override suspend fun exportTheme(file: ThemeFile): Boolean {
        val jsArray = JsArray<JsAny?>()
        jsArray[0] = json.encodeToString(file.theme).toJsString()
        val blob = Blob(jsArray, options = BlobPropertyBag(type = "application/json"))
        val url = URL.createObjectURL(blob)
        val a = document.createElement("a") as HTMLAnchorElement
        a.href = url
        a.download = "${file.name}.json"
        a.click()
        URL.revokeObjectURL(url)
        return false
    }

    @OptIn(ExperimentalWasmJsInterop::class)
    override suspend fun importTheme(): JsonTheme? {
        val input = document.createElement("input") as HTMLInputElement
        input.type = "file"
        input.accept = "application/json"

        input.onchange = {
            val files = input.files
            if ((files?.length ?: 0) > 0) {
                val file = files?.get(0)
                if (file != null) {
                    val reader = FileReader()
                    reader.onload = { _ ->
                        val buffer = reader.result?.unsafeCast<ArrayBuffer>()
                        if (buffer != null) {
                            val content = Int8Array(buffer).toByteArray().decodeToString()
                            val theme = json.decodeFromString<JsonTheme>(content)
                            mainViewModel.uiViewModel.setImportThemeDialogOpen(true)
                            mainViewModel.uiViewModel.setImportThemeData(theme)
                        }
                    }
                    reader.readAsArrayBuffer(file)
                }
            }
        }

        input.click()
        return null
    }

    override suspend fun listSelectedThemes(): List<String> = json.decodeFromString(localStorage.getItem("selected_themes") ?: "[]")

    override suspend fun setSelectedThemes(list: List<String>) {
        localStorage.setItem("selected_themes", json.encodeToString(list))
    }
}