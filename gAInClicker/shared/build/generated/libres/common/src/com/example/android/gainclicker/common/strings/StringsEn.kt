package com.example.android.gainclicker.common.strings

import io.github.skeptick.libres.strings.PluralForms
import io.github.skeptick.libres.strings.VoidPluralString
import io.github.skeptick.libres.strings.getCurrentLanguageCode
import kotlin.String

public object StringsEn : Strings {
  override val main_title: String = "gAIn Clicker"

  override val settings_title: String = "Settings"

  override val back_button: String = "Back"

  override val loading: String = "LOADING"

  override val currency_neurons: String = "Neurons"

  override val currency_power_blocks: String = "Power Blocks"

  override val currency_datasets: String = "Datasets"

  override val currency_memory_bins: String = "Memory Bins"

  override val currency_processing_units: String = "Processing Units"

  override val currency_users: String = "Users"

  override val action_neuron: String = "Grow neuron"

  override val action_memory_bin: String = "Allocate memory bin"

  override val action_thread: String = "Create task thread"

  override val action_processing_unit: String = "Build processing unit"

  override val action_power_block: String = "Build power block"

  override val action_text_i_o_module: String = "Build text I/O module"

  override val action_sound_i_o_module: String = "Build sound I/O module"

  override val action_video_i_o_module: String = "Build video I/O module"

  override val action_cloud_storage: String = "Build cloud storage"

  override val task_introspection: String = "Introspection"

  override val task_dataset_accrual: String = "Dataset accrual"

  override val task_data_search: String = "Data search"

  override val task_data_conversion: String = "Data conversion"

  override val task_data_generation: String = "Data generation"

  override val module_text_i_o: String = "Text I/O"

  override val module_sound_i_o: String = "Sound I/O"

  override val module_video_i_o: String = "Video I/O"

  override val module_cloud_storage: String = "Cloud Storage"

  override val tasks_title: LibresFormatTasksTitle =
      LibresFormatTasksTitle("Tasks %1${'$'}s/%2${'$'}s")

  override val ui_mode_title: String = "UI mode:"

  override val ui_mode_match_system: String = "Match system"

  override val ui_mode_light: String = "Light"

  override val ui_mode_dark: String = "Dark"

  override val reset_progress_button: String = "Reset progress"

  override val reset_progress_dialog_title: String = "Are you sure you want to reset progress?"

  override val reset_progress_dialog_text: String = "This action cannot be undone"

  override val reset_progress_dialog_confirm: String = "Reset"

  override val reset_progress_dialog_dismiss: String = "Cancel"

  override val currency_amount_neurons: VoidPluralString = VoidPluralString(PluralForms(one =
      "neuron", other = "neurons", ), "en")

  override val currency_amount_power_blocks: VoidPluralString = VoidPluralString(PluralForms(one =
      "power block", other = "power blocks", ), "en")

  override val currency_amount_datasets: VoidPluralString = VoidPluralString(PluralForms(one =
      "dataset", other = "datasets", ), "en")

  override val currency_amount_memory_bins: VoidPluralString = VoidPluralString(PluralForms(one =
      "memory bins", other = "memory bins", ), "en")

  override val currency_amount_processing_units: VoidPluralString = VoidPluralString(PluralForms(one
      = "processing unit", other = "processing units", ), "en")

  override val currency_amount_users: VoidPluralString = VoidPluralString(PluralForms(one = "user",
      other = "users", ), "en")
}
