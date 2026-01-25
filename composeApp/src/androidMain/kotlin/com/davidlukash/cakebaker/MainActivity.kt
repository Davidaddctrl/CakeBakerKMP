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
import com.davidlukash.cakebaker.data.AndroidSavesRepository
import com.davidlukash.cakebaker.data.EXPORT_SAVE
import com.davidlukash.cakebaker.data.IMPORT_SAVE
import com.davidlukash.cakebaker.data.log.Log
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.logger.AppLogger
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    var saveToBeExported: Save? = null
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val savesRepository = AndroidSavesRepository(baseDirectory = filesDir, activity = this)
        mainViewModel = MainViewModel(savesRepository)

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
        }
        if (requestCode == IMPORT_SAVE && resultCode == RESULT_OK) {
            CoroutineScope(Dispatchers.IO).launch {
                data?.data?.also { uri ->
                    val result = withResultSuspend {
                        contentResolver.openFileDescriptor(uri, "r")?.use {
                            FileInputStream(it.fileDescriptor).use { fileInputStream ->
                                val text = fileInputStream.readBytes().decodeToString()
                                val save = json.decodeFromString<Save>(text)
                                mainViewModel.uiViewModel.setImportDialogOpen(true)
                                mainViewModel.uiViewModel.setImportSaveData(save)
                            }
                        }
                    }
                    result.onFailure {
                        mainViewModel.uiViewModel.addTextPopup("Save Error")
                    }
                }
            }
        }
    }
}