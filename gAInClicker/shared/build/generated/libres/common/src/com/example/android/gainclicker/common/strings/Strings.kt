package com.example.android.gainclicker.common.strings

import io.github.skeptick.libres.strings.VoidPluralString
import kotlin.String

public interface Strings {
  public val main_title: String?

  public val settings_title: String?

  public val back_button: String?

  public val loading: String?

  public val currency_neurons: String?

  public val currency_power_blocks: String?

  public val currency_datasets: String?

  public val currency_memory_bins: String?

  public val currency_processing_units: String?

  public val currency_users: String?

  public val action_neuron: String?

  public val action_memory_bin: String?

  public val action_thread: String?

  public val action_processing_unit: String?

  public val action_power_block: String?

  public val action_text_i_o_module: String?

  public val action_sound_i_o_module: String?

  public val action_video_i_o_module: String?

  public val action_cloud_storage: String?

  public val task_introspection: String?

  public val task_dataset_accrual: String?

  public val task_data_search: String?

  public val task_data_conversion: String?

  public val task_data_generation: String?

  public val module_text_i_o: String?

  public val module_sound_i_o: String?

  public val module_video_i_o: String?

  public val module_cloud_storage: String?

  public val tasks_title: LibresFormatTasksTitle?

  public val ui_mode_title: String?

  public val ui_mode_match_system: String?

  public val ui_mode_light: String?

  public val ui_mode_dark: String?

  public val reset_progress_button: String?

  public val reset_progress_dialog_title: String?

  public val reset_progress_dialog_text: String?

  public val reset_progress_dialog_confirm: String?

  public val reset_progress_dialog_dismiss: String?

  public val currency_amount_neurons: VoidPluralString?

  public val currency_amount_power_blocks: VoidPluralString?

  public val currency_amount_datasets: VoidPluralString?

  public val currency_amount_memory_bins: VoidPluralString?

  public val currency_amount_processing_units: VoidPluralString?

  public val currency_amount_users: VoidPluralString?
}
