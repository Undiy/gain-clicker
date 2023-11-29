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

    override suspend fun updateGameState(updater: (GameState) -> GameState) {
        _gameState.update(updater)
    }

    override suspend fun updateDeposit(updater: (Deposit) -> Deposit) {
        _gameState.update { it.copy(deposit = updater(it.deposit)) }
    }

    override suspend fun updateModules(updater: (List<Module>) -> List<Module>) {
        _gameState.update { it.copy(modules = updater(it.modules)) }
    }

    override suspend fun updateTasks(updater: (TasksState) -> TasksState) {
        _gameState.update { it.copy(tasks = updater(it.tasks)) }
    }

    override suspend fun updateVisibleFeatures(updater: (VisibleFeatures) -> VisibleFeatures) {
        _gameState.update { it.copy(visibleFeatures = updater(it.visibleFeatures)) }
    }

}