package com.example.android.gainclicker

import com.example.android.gainclicker.data.inmemory.InMemoryGameStateRepository

class ServiceLocator {

    val gameStateRepository = InMemoryGameStateRepository()
}
