package di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import data.GameStateRepository
import data.realm.RealmGameStateRepository
import org.koin.dsl.module
import settings.DataStoreSettingsRepository
import settings.SettingsRepository
import settings.settingsDatastore
import ui.GAInClickerViewModel
import ui.settings.SettingsViewModel

val appModule = module {
    single<GameStateRepository> { RealmGameStateRepository() }
    single<DataStore<Preferences>> { settingsDatastore(get()) }
    single<SettingsRepository> { DataStoreSettingsRepository(get()) }

    factory<GAInClickerViewModel> { GAInClickerViewModel(get(), get()) }
    factory<SettingsViewModel> { SettingsViewModel(get(), get()) }
}