package com.example.android.gainclicker.koin

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.android.gainclicker.data.GameStateRepository
import com.example.android.gainclicker.data.realm.RealmGameStateRepository
import com.example.android.gainclicker.settings.DataStoreSettingsRepository
import com.example.android.gainclicker.settings.SettingsRepository
import com.example.android.gainclicker.settings.dataStore
import com.example.android.gainclicker.ui.GAInClickerViewModel
import com.example.android.gainclicker.ui.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single<GameStateRepository> { RealmGameStateRepository() }
    single<DataStore<Preferences>> { dataStore(get()) }
    single<SettingsRepository> { DataStoreSettingsRepository(get()) }
    viewModel { GAInClickerViewModel(get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }
}