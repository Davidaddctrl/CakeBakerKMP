package com.davidlukash.cakebaker.viewmodel

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidlukash.cakebaker.logger.AppLogger
import com.davidlukash.cakebaker.data.ConsoleType
import com.davidlukash.cakebaker.data.log.Log
import com.davidlukash.cakebaker.data.Popup
import com.davidlukash.cakebaker.data.UIActions
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.logger
import com.davidlukash.cakebaker.ui.input.SmallThemedButton
import com.davidlukash.cakebaker.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UIViewModel : ViewModel(), UIActions {
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

    private val _debugConsole = MutableStateFlow(ConsoleType.NONE)
    val debugConsole = _debugConsole.asStateFlow()

    private val _importDialogOpen = MutableStateFlow(false)
    private val _importSaveData = MutableStateFlow<Save?>(null)

    val importDialogOpen = _importDialogOpen.asStateFlow()
    val importSaveData = _importSaveData.asStateFlow()

    private val _variableShown = MutableStateFlow(false)
    val variableShown = _variableShown.asStateFlow()

    fun setVariableShown(variableShown: Boolean) {
        viewModelScope.launch {
            _variableShown.emit(variableShown)
        }
    }

    fun setImportDialogOpen(open: Boolean) {
        viewModelScope.launch {
            _importDialogOpen.emit(open)
        }
    }

    fun setImportSaveData(save: Save?) {
        viewModelScope.launch {
            _importSaveData.emit(save)
        }
    }

    override fun setDebugConsole(type: ConsoleType) {
        viewModelScope.launch {
            _debugConsole.emit(type)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun addPopup(shouldHaveDefaultButton: Boolean = true, content: @Composable Popup.() -> Unit) {
        viewModelScope.launch {
            _popups.emit(
                _popups.value + Popup(content, shouldHaveDefaultButton)
            )
        }
    }

    override fun addTextPopup(text: String) {
        addPopup {
            Text(
                text,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun addTextButtonPopup(text: String, shouldHaveDefaultButton: Boolean, buttonText: String, onClick: () -> Unit) {
        addPopup(shouldHaveDefaultButton) {
            Text(
                text,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            SmallThemedButton(
                onClick = {
                    onClick()
                    removePopup(uuid)
                },
                modifier = Modifier.fillMaxWidth(),
                content = {
                    Text(
                        buttonText,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
            )
        }
    }

    override fun getCurrentScreen(): Screen? {
        return _currentScreen.value
    }

    @OptIn(ExperimentalUuidApi::class)
    fun removePopup(uuid: Uuid) {
        viewModelScope.launch {
            _popups.emit(
                _popups.value.filter { it.uuid != uuid }
            )
        }
    }

    fun navigateTo(destination: Screen?) {
        viewModelScope.launch {
            _pendingScreen.emit(destination)
        }
    }

    fun navigateWithFade(destination: Screen?) {
        viewModelScope.launch {
            navigateTo(destination)
        }
    }

    /*This is used by the UI to inform viewmodels of the current screen*/
    fun updateCurrentScreen(screen: Screen) {
        viewModelScope.launch {
            _currentScreen.emit(screen)
        }
    }

    /*This is used by the UI to inform viewmodels of the true density so popups are not scaled*/
    fun updateTrueDensity(density: Density) {
        viewModelScope.launch {
            _trueDensity.emit(density)
        }
    }

    init {
        logger.registerLogger(
            object : AppLogger() {
                override fun appendLog(log: Log) {
                    viewModelScope.launch {
                        _logs.emit(
                            _logs.value + log
                        )
                    }
                }
            }
        )
    }
}
