package di

import data.GameStateRepository
import data.realm.RealmGameStateRepository
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import ui.GAInClickerViewModel
import ui.settings.SettingsViewModel

val commonModule = module {
    single<GameStateRepository> { RealmGameStateRepository() }

    factory { GAInClickerViewModel(get(), get()) }
    factory { SettingsViewModel(get(), get()) }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(commonModule, getDatastoreModuleByPlatform())
}

// called by iOS etc
fun initKoin() = initKoin{}