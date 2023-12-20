package di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import settings.DataStoreSettingsRepository
import settings.SettingsRepository
import settings.dataStoreFileName
import settings.getDataStore

actual fun getDatastoreModuleByPlatform() = module {

    single {
        getDataStore {
            androidContext().filesDir?.resolve(dataStoreFileName)?.absolutePath
                ?: throw Exception("Couldn't get Android Datastore context.")
        }
    }

    single<SettingsRepository> { DataStoreSettingsRepository(get()) }

}