package com.example.android.gainclicker

import com.example.android.gainclicker.data.realm.RealmGameStateRepository

class ServiceLocator {

    val gameStateRepository = RealmGameStateRepository()
}
