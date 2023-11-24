package com.example.android.gainclicker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.gainclicker.core.ClickAction
import com.example.android.gainclicker.core.GameState
import com.example.android.gainclicker.core.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

const val TASK_UPDATE_INTERVAL = 100

class GAInClickerViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()


    private val timer = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(TASK_UPDATE_INTERVAL.milliseconds)
        }
    }

    private val updater = viewModelScope.launch {
        withContext(Dispatchers.Default) {
            timer.collectLatest { timestamp ->
                _gameState.update {
                    it.updateTasks(timestamp)
                }
            }
        }
    }

    fun isActionVisible(action: ClickAction): Boolean {
        return action.isVisible(_gameState.value).also {visible ->
            if (visible && action !in _gameState.value.visibleFeatures.actions) {
                _gameState.update {
                    it.copy(visibleFeatures = it.visibleFeatures.copy(
                        actions = it.visibleFeatures.actions + action
                    ))
                }
            }
        }
    }

    fun isActionEnabled(action: ClickAction) = action.isAcquirable(_gameState.value)

    fun onActionClick(action: ClickAction) {
        _gameState.update {
            action.acquire(it)
        }
    }

    fun isTaskVisible(task: Task): Boolean {
        return task.isVisible(_gameState.value).also {visible ->
            if (visible && task !in _gameState.value.visibleFeatures.tasks) {
                _gameState.update {
                    it.copy(visibleFeatures = it.visibleFeatures.copy(
                        tasks = it.visibleFeatures.tasks + task
                    ))
                }
            }
        }
    }

    fun onTaskClick(task: Task) {
        _gameState.update {
            it.copy(
                tasks = it.tasks.toggleTaskThread(task)
            )
        }
    }
}