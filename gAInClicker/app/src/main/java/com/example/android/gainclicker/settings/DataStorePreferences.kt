package com.example.android.gainclicker.settings

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

private const val PREFERENCES_NAME = "preferences"
private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)

fun dataStore(context: Context) = context.dataStore
