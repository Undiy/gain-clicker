package com.example.android.gainclicker.ui

import com.example.android.gainclicker.core.ClickAction
import com.example.android.gainclicker.core.Currency

val Currency.title: String
    get() = when (this) {
        Currency.NEURON -> "Neurons"
        Currency.DATASET -> "Datasets"
        Currency.MEMORY_BIN -> "Memory Bins"
        Currency.PROCESSING_UNIT -> "Processing Units"
        Currency.USER -> "Users"
    }

val ClickAction.title: String
    get() = when (this) {
        ClickAction.NEURON -> "Grow neuron"
        ClickAction.MEMORY_BIN -> "Allocate memory bin"
        ClickAction.PROCESSING_UNIT -> "Build processing unit"
        ClickAction.IO_MODULE_TEXT -> "Build text I/O module"
        ClickAction.IO_MODULE_SOUND -> "Build sound I/O module"
        ClickAction.IO_MODULE_VIDEO -> "Build video I/O module"
        ClickAction.THREAD -> "Allocate thread slot"
        ClickAction.CLOUD_STORAGE -> "Build cloud storage"
    }