package undiy.games.gainclicker

import android.app.Application
import di.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class GAInClickerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        }

        initKoin {
            androidLogger()
            androidContext(this@GAInClickerApplication)
        }
    }
}