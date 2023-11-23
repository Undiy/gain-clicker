package com.example.android.gainclicker.ui

import androidx.lifecycle.ViewModel
import com.example.android.gainclicker.core.ClickAction
import com.example.android.gainclicker.core.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GAInClickerViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    fun onActionClick(action: ClickAction) {
        _gameState.update {
            action.acquire(it)
        }
    }
}