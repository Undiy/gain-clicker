package com.example.android.gainclicker

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.android.gainclicker.data.realm.RealmGameStateRepository
import com.example.android.gainclicker.settings.DataStoreSettingsRepository

private const val PREFERENCES_NAME = "preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = PREFERENCES_NAME
)

class ServiceLocator(context: Context) {

    val gameStateRepository = RealmGameStateRepository()
    val settingsRepository = DataStoreSettingsRepository(context.dataStore)
}
