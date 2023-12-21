package di

import org.koin.dsl.module
import settings.DataStoreSettingsRepository
import settings.SettingsRepository
import settings.dataStoreFileName
import settings.getDataStore
import java.nio.file.Paths

actual fun getDatastoreModuleByPlatform() = module {

    single {
        getDataStore {
            val currentPath = Paths.get(System.getProperty("user.dir")).toString()
            Paths.get(currentPath, dataStoreFileName).toString()
        }
    }

    single<SettingsRepository> { DataStoreSettingsRepository(get()) }

}
