package com.example.android.gainclicker.ui

import com.example.android.gainclicker.core.ClickAction
import com.example.android.gainclicker.core.CloudStorage
import com.example.android.gainclicker.core.Currency
import com.example.android.gainclicker.core.IOModule
import com.example.android.gainclicker.core.Module
import com.example.android.gainclicker.core.Task

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

val Task.title: String
    get() = when (this) {
        Task.INTROSPECTION -> "Introspection"
        Task.DATASET_ACCRUAL -> "Dataset accrual"
        Task.DATA_SEARCH -> "Data search"
        Task.DATA_CONVERSION -> "Data conversion"
        Task.DATA_GENERATION -> "Data generation"
    }

val Module.title: String
    get() = when(this) {
        IOModule.IO_TEXT -> "Text I/O"
        IOModule.IO_SOUND -> "Sound I/O"
        IOModule.IO_VIDEO -> "Video I/O"
        is CloudStorage -> "Cloud Storage"
    }