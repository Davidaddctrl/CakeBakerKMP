package com.davidlukash.cakebaker.data

import com.davidlukash.cakebaker.json
import com.davidlukash.cakebaker.mainViewModel
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

class JSSavesRepository : SavesRepository() {
    private val localStorage = window.localStorage

    private fun updateSaves(list: List<SaveFile>) {
        localStorage.setItem("saves", json.encodeToString(list))
    }

    override fun listSaves(): List<SaveFile> {
        val savesString = localStorage.getItem("saves") ?: "[]"
        val saves = json.decodeFromString<List<SaveFile>>(savesString)
        return saves
    }

    override fun deleteSave(name: String): Boolean {
        val saves = listSaves()
        if (!saves.map { it.name }.contains(name)) return false
        updateSaves(
            saves.filterNot { it.name == name }
        )
        return true
    }

    override fun upsertSave(file: SaveFile): Boolean {
        val saves = listSaves()
        val existsBefore = saves.map { it.name }.contains(file.name)
        updateSaves((listOf(file) + saves).distinctBy { it.name })
        return existsBefore
    }

    override fun exportSave(file: SaveFile): Boolean {
        val blob = Blob(arrayOf(json.encodeToString(file.save)), options = BlobPropertyBag(type = "application/json"))
        val url = URL.createObjectURL(blob)
        val a = document.createElement("a") as HTMLAnchorElement
        a.href = url
        a.download = "${file.name}.json"
        a.click()
        URL.revokeObjectURL(url)
        return false
    }

    override fun importSave(): Save? {
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
                            val save = json.decodeFromString<Save>(content)
                            mainViewModel.uiViewModel.setImportDialogOpen(true)
                            mainViewModel.uiViewModel.setImportSaveData(save)
                        }
                    }
                    reader.readAsArrayBuffer(file)
                }
            }
        }

        input.click()
        return null
    }
}