package settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreSettingsRepository(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private companion object {
        val UI_MODE_KEY = stringPreferencesKey("ui_mode")
    }

    override val uiMode: Flow<UiMode> = dataStore.data.map { preferences ->
        UiMode.valueOf(preferences[UI_MODE_KEY] ?: UiMode.SYSTEM.name)
    }

    override suspend fun setUiMode(uiMode: UiMode) {
        dataStore.edit { preferences ->
            preferences[UI_MODE_KEY] = uiMode.name
        }
    }
}