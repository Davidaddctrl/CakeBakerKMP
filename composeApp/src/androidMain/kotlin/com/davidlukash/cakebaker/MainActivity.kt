package com.davidlukash.cakebaker

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.davidlukash.cakebaker.repository.AndroidSavesRepository
import com.davidlukash.cakebaker.repository.EXPORT_SAVE
import com.davidlukash.cakebaker.repository.IMPORT_SAVE
import com.davidlukash.cakebaker.data.log.Log
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.theme.json.JsonTheme
import com.davidlukash.cakebaker.logger.AppLogger
import com.davidlukash.cakebaker.repository.AndroidThemesRepository
import com.davidlukash.cakebaker.repository.EXPORT_THEME
import com.davidlukash.cakebaker.repository.IMPORT_THEME
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    var saveToBeExported: Save? = null
    var themeToBeExported: JsonTheme? = null
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        WindowCompat.setDecorFitsSystemWindows(window, false)

        CoroutineScope(Dispatchers.IO).launch {
            withResultSuspend {
                Save.readOldSaves()
            }
        }

        val savesRepository = AndroidSavesRepository(baseDirectory = filesDir, activity = this)
        val themesRepository = AndroidThemesRepository(baseDirectory = filesDir, activity = this)
        mainViewModel = MainViewModel(savesRepository, themesRepository)

        logger.registerLogger(
            object : AppLogger() {
                override fun appendLog(log: Log) {
                    val logFile = filesDir.resolve("log.txt").also { it.createNewFile() }
                    logFile.appendText("${log.toLogString()}\n")
                }
            }
        )

        setContent {
            CompositionLocalProvider(
                LocalMainViewModel provides mainViewModel
            ) {
                App()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (this::mainViewModel.isInitialized)
            mainViewModel.dataViewModel.startLoop()
    }

    override fun onStop() {
        super.onStop()
        if (this::mainViewModel.isInitialized)
            mainViewModel.dataViewModel.stopLoop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, caller: ComponentCaller) {
        super.onActivityResult(requestCode, resultCode, data, caller)
        if (requestCode == EXPORT_SAVE && resultCode == RESULT_OK) {
            val content = saveToBeExported?.let { json.encodeToString(it) }
            CoroutineScope(Dispatchers.IO).launch {
                content?.let {
                    data?.data?.also { uri ->
                        val result = withResultSuspend {
                            contentResolver.openFileDescriptor(uri, "w")?.use {
                                FileOutputStream(it.fileDescriptor).use { fileOutputStream ->
                                    fileOutputStream.write(content.toByteArray())
                                }
                            }
                        }
                        result.onSuccess {
                            mainViewModel.uiViewModel.addTextPopup("Save Exported")
                        }
                        result.onFailure {
                            mainViewModel.uiViewModel.addTextPopup("Save Error")
                        }
                    }
                }
            }
        } else if (requestCode == IMPORT_SAVE && resultCode == RESULT_OK) {
            CoroutineScope(Dispatchers.IO).launch {
                data?.data?.also { uri ->
                    val result = withResultSuspend {
                        contentResolver.openFileDescriptor(uri, "r")?.use {
                            FileInputStream(it.fileDescriptor).use { fileInputStream ->
                                val text = fileInputStream.readBytes().decodeToString()
                                val save = json.decodeFromString<Save>(text)
                                mainViewModel.uiViewModel.setImportSaveDialogOpen(true)
                                mainViewModel.uiViewModel.setImportSaveData(save)
                            }
                        }
                    }
                    result.onFailure {
                        mainViewModel.uiViewModel.addTextPopup("Save Error")
                    }
                }
            }
        } else if (requestCode == EXPORT_THEME && resultCode == RESULT_OK) {
            val content = themeToBeExported?.let { json.encodeToString(it) }
            CoroutineScope(Dispatchers.IO).launch {
                content?.let {
                    data?.data?.also { uri ->
                        val result = withResultSuspend {
                            contentResolver.openFileDescriptor(uri, "w")?.use {
                                FileOutputStream(it.fileDescriptor).use { fileOutputStream ->
                                    fileOutputStream.write(content.toByteArray())
                                }
                            }
                        }
                        result.onSuccess {
                            mainViewModel.uiViewModel.addTextPopup("Theme Exported")
                        }
                        result.onFailure {
                            mainViewModel.uiViewModel.addTextPopup("Theme Error")
                        }
                    }
                }
            }
        } else if (requestCode == IMPORT_THEME && resultCode == RESULT_OK) {
            CoroutineScope(Dispatchers.IO).launch {
                data?.data?.also { uri ->
                    val result = withResultSuspend {
                        contentResolver.openFileDescriptor(uri, "r")?.use {
                            FileInputStream(it.fileDescriptor).use { fileInputStream ->
                                val text = fileInputStream.readBytes().decodeToString()
                                val theme = json.decodeFromString<JsonTheme>(text)
                                mainViewModel.uiViewModel.setImportThemeDialogOpen(true)
                                mainViewModel.uiViewModel.setImportThemeData(theme)
                            }
                        }
                    }
                    result.onFailure {
                        mainViewModel.uiViewModel.addTextPopup("Theme Error")
                    }
                }
            }
        }
    }
}