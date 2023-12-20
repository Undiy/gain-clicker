package di

import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import settings.DataStoreSettingsRepository
import settings.SettingsRepository
import settings.dataStoreFileName
import settings.getDataStore

actual fun getDatastoreModuleByPlatform() = module {

    single {
        getDataStore {
            val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            requireNotNull(documentDirectory).path + "/$dataStoreFileName"
        }
    }

    single<SettingsRepository> { DataStoreSettingsRepository(get()) }

}
