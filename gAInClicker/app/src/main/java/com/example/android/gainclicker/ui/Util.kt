package com.example.android.gainclicker.ui

import com.example.android.gainclicker.core.Currency

val Currency.title: String
    get() = when (this) {
        Currency.NEURON -> "Neurons"
        Currency.DATASET -> "Datasets"
        Currency.MEMORY_BIN -> "Memory Bins"
        Currency.PROCESSING_UNIT -> "Processing Units"
        Currency.USER -> "Users"
    }