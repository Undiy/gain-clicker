package com.example.android.gainclicker.ui

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

const val TASK_UPDATE_INTERVAL = 250

class GAInClickerViewModel(
    private val gameStateRepository: GameStateRepository
) : ViewModel() {

    val gameState: StateFlow<GameState> = gameStateRepository.gameState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GameState(updatedAt = 0)
        )

    private val timer = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(TASK_UPDATE_INTERVAL.milliseconds)
        }
    }

    private val updater = viewModelScope.launch {
        withContext(Dispatchers.Default) {
            timer.collectLatest { timestamp ->
                gameStateRepository.updateGameState {
                    it.updateProgress(timestamp)
                }
            }
        }
    }

    fun isActionVisible(action: ClickAction): Boolean {
        return action.isVisible(gameState.value).also {visible ->
            if (visible && action !in gameState.value.visibleFeatures.actions) {
                viewModelScope.launch {
                    gameStateRepository.updateVisibleFeatures {
                        it.copy(actions = it.actions + action)
                    }
                }
            }
        }
    }

    fun isActionEnabled(action: ClickAction) = action.isAcquirable(gameState.value)

    fun onActionClick(action: ClickAction) {
        viewModelScope.launch {
            gameStateRepository.updateGameState {
                action.acquire(it)
            }
        }
    }

    fun isTasksViewVisible(): Boolean {
        return Task.values().any { it.isVisible(gameState.value) }
    }

    fun isTaskVisible(task: Task): Boolean {
        return task.isVisible(gameState.value).also { visible ->
            gameState.value.visibleFeatures.let { visibleFeatures ->
                if (visible && task !in visibleFeatures.tasks) {
                    viewModelScope.launch {
                        gameStateRepository.updateVisibleFeatures {
                            it.copy(
                                tasks = visibleFeatures.tasks + task
                            )
                        }
                    }
                }
            }
        }
    }

    fun onTaskClick(task: Task) {
        viewModelScope.launch {
            gameStateRepository.updateTasks {
                it.toggleTaskThread(task)
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

    companion object {
        val factory = viewModelFactory {
            // Initializer for ItemEditViewModel
            initializer {
                GAInClickerViewModel(
                    (this[
                        ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY
                    ] as GAInClickerApplication)
                        .serviceLocator.gameStateRepository
                )
            }
        }
    }
}
