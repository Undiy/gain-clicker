package settings

import kotlinx.coroutines.flow.Flow

enum class UiMode {
    SYSTEM,
    LIGHT,
    DARK,
}

interface SettingsRepository {

    val uiMode: Flow<UiMode>

    suspend fun setUiMode(uiMode: UiMode)
}