package com.davidlukash.cakebaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.davidlukash.cakebaker.data.AndroidSavesRepository
import com.davidlukash.cakebaker.viewmodel.LocalMainViewModel
import com.davidlukash.cakebaker.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        val savesRepository = AndroidSavesRepository(filesDir)
        mainViewModel = MainViewModel(savesRepository)

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

}