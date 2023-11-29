package com.example.android.gainclicker.data

import com.example.android.gainclicker.core.Deposit
import com.example.android.gainclicker.core.GameState
import com.example.android.gainclicker.core.Module
import com.example.android.gainclicker.core.TasksState
import com.example.android.gainclicker.core.VisibleFeatures
import kotlinx.coroutines.flow.Flow

interface GameStateRepository {

    val gameState: Flow<GameState>

    suspend fun updateGameState(updater: (GameState) -> GameState)

    suspend fun updateDeposit(updater: (Deposit) -> Deposit)

    suspend fun updateModules(updater: (List<Module>) -> (List<Module>))

    suspend fun updateTasks(updater: (TasksState) -> TasksState)

    suspend fun updateVisibleFeatures(updater: (VisibleFeatures) -> VisibleFeatures)
}