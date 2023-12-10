package com.example.android.gainclicker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.android.gainclicker.GAInClickerApplication
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

    companion object {
        val factory = viewModelFactory {
            // Initializer for ItemEditViewModel
            initializer {
                (this[
                    ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY
                ] as GAInClickerApplication).let { application ->
                    SettingsViewModel(
                        application.serviceLocator.gameStateRepository,
                        application.serviceLocator.settingsRepository
                    )
                }
            }
        }
    }
}
