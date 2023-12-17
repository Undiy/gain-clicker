package settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


private const val dataStoreFileName = "preferences"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(dataStoreFileName)

fun settingsDatastore(context: Context) = context.dataStore

