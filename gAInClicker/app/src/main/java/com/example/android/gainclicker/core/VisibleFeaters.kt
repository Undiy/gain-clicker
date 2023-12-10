package com.example.android.gainclicker.core


data class VisibleFeatures(
    val tasks: Set<Task> = setOf(),
    val actions: Set<ClickAction> = setOf()
)
