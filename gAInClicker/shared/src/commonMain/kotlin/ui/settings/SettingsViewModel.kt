package ui.settings

import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import core.GameState
import data.GameStateRepository
import settings.SettingsRepository
import settings.UiMode
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
