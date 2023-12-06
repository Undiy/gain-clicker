package com.example.android.gainclicker

import android.app.Application

class GAInClickerApplication : Application() {

    lateinit var serviceLocator: ServiceLocator

    override fun onCreate() {
        super.onCreate()
        serviceLocator = ServiceLocator(this)
    }
}