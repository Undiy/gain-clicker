package com.example.android.gainclicker.data.inmemory

import com.example.android.gainclicker.core.Deposit
import com.example.android.gainclicker.core.GameState
import com.example.android.gainclicker.core.Module
import com.example.android.gainclicker.core.TasksState
import com.example.android.gainclicker.core.VisibleFeatures
import com.example.android.gainclicker.data.GameStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InMemoryGameStateRepository(
    initialGameState: GameState = GameState()
) : GameStateRepository {

    private val _gameState = MutableStateFlow(initialGameState)
    override val gameState: Flow<GameState>
        get() = _gameState.asStateFlow()

    override suspend fun updateGameState(newGameState: GameState) {
        _gameState.update { newGameState }
    }

    override suspend fun updateDeposit(deposit: Deposit) {
        _gameState.update { it.copy(deposit = deposit) }
    }

    override suspend fun updateModules(modules: List<Module>) {
        _gameState.update { it.copy(modules = modules) }
    }

    override suspend fun updateTasks(tasks: TasksState) {
        _gameState.update { it.copy(tasks = tasks) }
    }

    override suspend fun updateVisibleFeatures(visibleFeatures: VisibleFeatures) {
        _gameState.update { it.copy(visibleFeatures = visibleFeatures) }
    }

}