package com.davidlukash.cakebaker.viewmodel

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidlukash.cakebaker.AppLogger
import com.davidlukash.cakebaker.data.ConsoleType
import com.davidlukash.cakebaker.data.Log
import com.davidlukash.cakebaker.data.Popup
import com.davidlukash.cakebaker.ui.LocalFontFamily
import com.davidlukash.cakebaker.ui.SmallThemedButton
import com.davidlukash.cakebaker.ui.navigation.FadeScreen
import com.davidlukash.cakebaker.ui.navigation.Screen
import com.davidlukash.cakebaker.ui.navigation.transitionDuration
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UIViewModel : ViewModel(), AppLogger {
    private val _pendingScreen = MutableStateFlow<Screen?>(null)
    val pendingScreen = _pendingScreen.asStateFlow()

    private val _popups = MutableStateFlow(emptyList<Popup>())
    val popups = _popups.asStateFlow()

    private val _trueDensity = MutableStateFlow<Density?>(null)
    val trueDensity = _trueDensity.asStateFlow()

    private val _currentScreen = MutableStateFlow<Screen?>(null)
    val currentScreen = _currentScreen.asStateFlow()

    private val _logs = MutableStateFlow(listOf<Log>())
    val logs = _logs.asStateFlow()

    private val _debugConsole = MutableStateFlow(ConsoleType.SIDEBAR)
    val debugConsole = _debugConsole.asStateFlow()

    private var nextId = 0

    override fun appendLog(log: Log) {
        viewModelScope.launch {
            _logs.emit(
                _logs.value + log
            )
        }
    }

    override fun setDebugConsole(type: ConsoleType) {
        viewModelScope.launch {
            _debugConsole.emit(type)
        }
    }

    override fun getDebugConsole(): ConsoleType = debugConsole.value

    fun addPopup(shouldHaveDefaultButton: Boolean = true, content: @Composable Popup.() -> Unit) {
        viewModelScope.launch {
            _popups.emit(
                _popups.value + Popup(content, nextId++, shouldHaveDefaultButton)
            )
        }
    }

    fun addTextPopup(text: String) {
        addPopup {
            Text(
                text,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    fun addTextButtonPopup(text: String, shouldHaveDefaultButton: Boolean = true, buttonText: String, onClick: () -> Unit) {
        addPopup(shouldHaveDefaultButton) {
            val viewModel = LocalMainViewModel.current
            val themeViewModel = viewModel.themeViewModel
            val theme by themeViewModel.theme.collectAsState()
            Text(
                text,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            SmallThemedButton(
                onClick = {
                    onClick()
                    removePopup(this.index)
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    buttonText,
                    style = theme.smallLabelStyle,
                    fontFamily = LocalFontFamily.current,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    fun removePopup(index: Int) {
        viewModelScope.launch {
            _popups.emit(
                _popups.value.filterIndexed { thisIndex, _ -> thisIndex != index }
            )
        }
    }

    fun navigateTo(destination: Screen?) {
        viewModelScope.launch {
            _pendingScreen.value = destination
        }
    }

    fun navigateWithFade(destination: Screen?) {
        viewModelScope.launch {
            navigateTo(FadeScreen)
            delay(transitionDuration.toLong())
            navigateTo(destination)
        }
    }

    /*This is used by the UI to inform viewmodels of the current screen*/
    fun updateCurrentScreen(screen: Screen) {
        viewModelScope.launch {
            val previousScreen = _currentScreen.value
            if (previousScreen == FadeScreen) delay(transitionDuration.toLong())
            _currentScreen.emit(screen)
        }
    }

    /*This is used by the UI to inform viewmodels of the true density so popups are not scaled*/
    fun updateTrueDensity(density: Density) {
        viewModelScope.launch {
            _trueDensity.emit(density)
        }
    }
}
