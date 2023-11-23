package com.example.android.gainclicker.core

enum class Module(
    val isIo: Boolean = false
) {
    IO_TEXT(true),
    IO_SOUND(true),
    IO_VIDEO(true),
    CLOUD_STORAGE
}