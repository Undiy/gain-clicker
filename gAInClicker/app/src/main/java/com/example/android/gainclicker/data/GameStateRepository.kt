package com.example.android.gainclicker.data

import com.example.android.gainclicker.core.Deposit
import com.example.android.gainclicker.core.GameState
import com.example.android.gainclicker.core.Module
import com.example.android.gainclicker.core.TasksState
import com.example.android.gainclicker.core.VisibleFeatures
import kotlinx.coroutines.flow.Flow

interface GameStateRepository {

    val gameState: Flow<GameState>

    suspend fun updateGameState(newGameState: GameState)

    suspend fun updateDeposit(deposit: Deposit)

    suspend fun updateModules(modules: List<Module>)

    suspend fun updateTasks(tasks: TasksState)

    suspend fun updateVisibleFeatures(visibleFeatures: VisibleFeatures)
}