package com.example.android.gainclicker.ui

import com.example.android.gainclicker.Res
import com.example.android.gainclicker.core.ClickAction
import com.example.android.gainclicker.core.CloudStorage
import com.example.android.gainclicker.core.Currency
import com.example.android.gainclicker.core.CurrencyAmount
import com.example.android.gainclicker.core.IOModule
import com.example.android.gainclicker.core.Module
import com.example.android.gainclicker.core.Task
import com.example.android.gainclicker.core.TasksState
import com.example.android.gainclicker.settings.UiMode

val Currency.title: String
    get() = when (this) {
        Currency.NEURON -> Res.string.currency_neurons
        Currency.POWER_BLOCK -> Res.string.currency_power_blocks
        Currency.DATASET -> Res.string.currency_datasets
        Currency.MEMORY_BIN -> Res.string.currency_memory_bins
        Currency.PROCESSING_UNIT -> Res.string.currency_processing_units
        Currency.USER -> Res.string.currency_users
    }

val CurrencyAmount.currencyTitle
    get() = when (currency) {
        Currency.NEURON -> Res.string.currency_amount_neurons
        Currency.POWER_BLOCK -> Res.string.currency_amount_power_blocks
        Currency.DATASET -> Res.string.currency_amount_datasets
        Currency.MEMORY_BIN -> Res.string.currency_amount_memory_bins
        Currency.PROCESSING_UNIT -> Res.string.currency_amount_processing_units
        Currency.USER -> Res.string.currency_amount_users
    }.format(value)

val ClickAction.title: String
    get() = when (this) {
        ClickAction.NEURON -> Res.string.action_neuron
        ClickAction.MEMORY_BIN -> Res.string.action_memory_bin
        ClickAction.THREAD_FIRST -> Res.string.action_thread
        ClickAction.PROCESSING_UNIT -> Res.string.action_processing_unit
        ClickAction.POWER_BLOCK -> Res.string.action_power_block
        ClickAction.IO_MODULE_TEXT -> Res.string.action_text_i_o_module
        ClickAction.IO_MODULE_SOUND -> Res.string.action_sound_i_o_module
        ClickAction.IO_MODULE_VIDEO -> Res.string.action_video_i_o_module
        ClickAction.THREAD -> Res.string.action_thread
        ClickAction.CLOUD_STORAGE -> Res.string.action_cloud_storage
    }

val Task.title: String
    get() = when (this) {
        Task.INTROSPECTION -> Res.string.task_introspection
        Task.DATASET_ACCRUAL -> Res.string.task_dataset_accrual
        Task.DATA_SEARCH -> Res.string.task_data_search
        Task.DATA_CONVERSION -> Res.string.task_data_conversion
        Task.DATA_GENERATION -> Res.string.task_data_generation
    }

val Module.title: String
    get() = when(this) {
        IOModule.IO_TEXT -> Res.string.module_text_i_o
        IOModule.IO_SOUND -> Res.string.module_sound_i_o
        IOModule.IO_VIDEO -> Res.string.module_video_i_o
        is CloudStorage -> Res.string.module_cloud_storage
    }

val TasksState.title: String
    get() = Res.string.tasks_title.format(
        tasks = "${taskThreads.size}",
        threads = "$threadSlots"
    )

val UiMode.title: String
    get() = when(this) {
        UiMode.SYSTEM -> Res.string.ui_mode_match_system
        UiMode.LIGHT -> Res.string.ui_mode_light
        UiMode.DARK -> Res.string.ui_mode_dark
    }