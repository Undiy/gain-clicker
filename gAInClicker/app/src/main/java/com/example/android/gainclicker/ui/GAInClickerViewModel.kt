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
    lateinit var gameState: StateFlow<GameState>

    init {
        viewModelScope.launch {
            gameState = gameStateRepository.gameState
                .stateIn(viewModelScope)
        }
    }

    private val timer = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(TASK_UPDATE_INTERVAL.milliseconds)
        }
    }

    private val updater = viewModelScope.launch {
        withContext(Dispatchers.Default) {
            timer.collectLatest { timestamp ->
                gameStateRepository.updateGameState(
                    gameState.value.updateProgress(timestamp)
                )
            }
        }
    }

    fun isActionVisible(action: ClickAction): Boolean {
        return action.isVisible(gameState.value).also {visible ->
            if (visible && action !in gameState.value.visibleFeatures.actions) {
                gameStateRepository.updateVisibleFeatures(
                    gameState.value.visibleFeatures.let {
                        it.copy(
                            actions = it.actions + action
                        )
                    }
                )
            }
        }
    }

    fun isActionEnabled(action: ClickAction) = action.isAcquirable(gameState.value)

    fun onActionClick(action: ClickAction) {
        gameStateRepository.updateGameState(action.acquire(gameState.value))
    }

    fun isTaskVisible(task: Task): Boolean {
        return task.isVisible(gameState.value).also { visible ->
            gameState.value.visibleFeatures.let { visibleFeatures ->
                if (visible && task !in visibleFeatures.tasks) {
                    gameStateRepository.updateVisibleFeatures(
                        visibleFeatures.copy(
                            tasks = visibleFeatures.tasks + task
                        )
                    )
                }
            }
        }
    }

    fun onTaskClick(task: Task) {
        gameStateRepository.updateTasks(
            gameState.value.tasks.toggleTaskThread(task)
        )
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