package com.example.android.gainclicker.data

import com.example.android.gainclicker.core.Deposit
import com.example.android.gainclicker.core.GameState
import com.example.android.gainclicker.core.Module
import com.example.android.gainclicker.core.TaskThreadsState
import com.example.android.gainclicker.core.VisibleFeatures
import kotlinx.coroutines.flow.Flow

interface GameStateRepository {

    val gameState: Flow<GameState>

    fun updateGameState(newGameState: GameState)

    fun updateDeposit(deposit: Deposit)

    fun updateModules(modules: List<Module>)

    fun updateTasks(tasks: TaskThreadsState)

    fun updateVisibleFeatures(visibleFeatures: VisibleFeatures)
}