package com.example.android.gainclicker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.gainclicker.core.GameState
import com.example.android.gainclicker.data.GameStateRepository
import com.example.android.gainclicker.settings.SettingsRepository
import com.example.android.gainclicker.settings.UiMode
import kotlinx.coroutines.launch


class SettingsViewModel(
    private val gameStateRepository: GameStateRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel(){

    fun setUiMode(uiMode: UiMode) {
        viewModelScope.launch {
            settingsRepository.setUiMode(uiMode)
        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            gameStateRepository.updateGameState { GameState() }
        }
    }
}
