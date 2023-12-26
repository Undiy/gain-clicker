package data

import core.Deposit
import core.GameState
import core.Module
import core.TasksState
import core.VisibleFeatures
import kotlinx.coroutines.flow.Flow

interface GameStateRepository {

    val gameState: Flow<GameState>

    suspend fun updateGameState(updater: (GameState) -> GameState)

    suspend fun updateDeposit(updater: (Deposit) -> Deposit)

    suspend fun updateModules(updater: (List<Module>) -> (List<Module>))

    suspend fun updateTasks(updater: (TasksState) -> TasksState)

    suspend fun updateVisibleFeatures(updater: (VisibleFeatures) -> VisibleFeatures)

    fun close() {}
}