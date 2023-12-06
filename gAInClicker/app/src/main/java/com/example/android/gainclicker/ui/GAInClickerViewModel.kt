package com.example.android.gainclicker.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.android.gainclicker.GAInClickerApplication
import com.example.android.gainclicker.core.ClickAction
import com.example.android.gainclicker.core.CloudStorage
import com.example.android.gainclicker.core.GameState
import com.example.android.gainclicker.core.IOModule
import com.example.android.gainclicker.core.Module
import com.example.android.gainclicker.core.Task
import com.example.android.gainclicker.data.GameStateRepository
import com.example.android.gainclicker.settings.UiMode
import com.example.android.gainclicker.settings.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

const val PROGRESS_UPDATE_INTERVAL = 500
const val SAVE_STATE_INTERVAL = 10_000L

class GAInClickerViewModel(
    private val gameStateRepository: GameStateRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel(){

    private val _gameState = MutableStateFlow(GameState(updatedAt = 0L))
    private var loadedAt: Long? = null

    val gameState: StateFlow<GameState>
        get() = _gameState

    private val timer = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(PROGRESS_UPDATE_INTERVAL.milliseconds)
        }
    }

    private var loader: Job? = null

    private val stateLoaded: Boolean
        get() = loadedAt != null
    private var updater: Job? = null

    private fun startUpdater() {
        updater = viewModelScope.launch {
            withContext(Dispatchers.Default) {
                timer.collect { timestamp ->
                    if (stateLoaded) {
                        _gameState.update {
                            it.updateProgress(timestamp)
                        }
                        if (_gameState.value.updatedAt - loadedAt!! >= SAVE_STATE_INTERVAL) {
                            loadedAt = null
                            gameStateRepository.updateGameState { _gameState.value }
                            Log.i("ViewModel", "State saved with ts ${_gameState.value.updatedAt}")
                        }
                    } else {
                        Log.i("PROGRESS", "Skip update: not loaded")
                    }
                }
            }
        }
    }
    private suspend fun stopUpdater() {
        updater?.cancelAndJoin()
        updater = null
    }


    private fun startLoader() {
        loader = viewModelScope.launch {
            gameStateRepository.gameState.collect { loadedGameState ->
                loadedAt = _gameState.updateAndGet {
                    loadedGameState
                }.updatedAt
                Log.i("ViewModel", "State loaded with ts $loadedAt")
            }
        }
    }

    private suspend fun stopLoader() {
        loader?.cancelAndJoin()
        loader = null
    }


    fun onStart() {
        Log.i("ViewModel", "onStart")
        startLoader()
        startUpdater()
    }

    fun onStop() {
        Log.i("ViewModel", "onStop")
        viewModelScope.launch {
            stopUpdater()
            Log.i("ViewModel", "onStop: stopped updater")
            stopLoader()
            Log.i("ViewModel", "onStop: stopped loader")
            gameStateRepository.updateGameState { _gameState.value }
            Log.i("ViewModel", "onStop: saved state")
        }
    }

    fun isActionVisible(action: ClickAction): Boolean {
        return action.isVisible(gameState.value).also {visible ->
            if (visible && action !in gameState.value.visibleFeatures.actions) {
                _gameState.update {
                    it.copy(
                        visibleFeatures = it.visibleFeatures.copy(
                            actions = it.visibleFeatures.actions + action
                        )
                    )
                }
            }
        }
    }

    fun isActionEnabled(action: ClickAction) = action.isAcquirable(gameState.value)

    fun onActionClick(action: ClickAction) {
            _gameState.update {
                action.acquire(it)
            }
    }

    fun isTasksViewVisible(): Boolean {
        return gameState.value.tasks.threadSlots > 0
    }

    fun isTaskVisible(task: Task): Boolean {
        return task.isVisible(gameState.value).also { visible ->
            gameState.value.visibleFeatures.let { visibleFeatures ->
                if (visible && task !in visibleFeatures.tasks) {
                    _gameState.update {
                        it.copy(
                            visibleFeatures = it.visibleFeatures.copy(
                                tasks = it.visibleFeatures.tasks + task
                            )
                        )
                    }
                }
            }
        }
    }

    fun onTaskClick(task: Task) {
        viewModelScope.launch {
            _gameState.update {
                it.copy(
                    tasks = it.tasks.toggleTaskThread(task)
                )
            }
        }
    }

    fun isModuleVisible(module: Module): Boolean {
        return when (module) {
            IOModule.IO_TEXT -> ClickAction.IO_MODULE_TEXT
            IOModule.IO_SOUND -> ClickAction.IO_MODULE_SOUND
            IOModule.IO_VIDEO -> ClickAction.IO_MODULE_VIDEO
            is CloudStorage -> ClickAction.CLOUD_STORAGE
        } in gameState.value.visibleFeatures.actions
    }

    fun isModuleEnabled(module: Module) = module in gameState.value.modules

    // Settings

    val uiMode = settingsRepository.uiMode

    fun setUiMode(uiMode: UiMode) {
        viewModelScope.launch {
            settingsRepository.setUiMode(uiMode)
        }
    }

    companion object {
        val factory = viewModelFactory {
            // Initializer for ItemEditViewModel
            initializer {
                (this[
                    ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY
                ] as GAInClickerApplication).let { application ->
                    GAInClickerViewModel(
                        application.serviceLocator.gameStateRepository,
                        application.serviceLocator.settingsRepository
                    )
                }

            }
        }
    }
}
