package com.example.android.gainclicker

import android.app.Application
import com.example.android.gainclicker.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GAInClickerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@GAInClickerApplication)
            modules(appModule)
        }
    }
}