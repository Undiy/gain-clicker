package com.example.android.gainclicker.common.strings

import io.github.skeptick.libres.strings.PluralForms
import io.github.skeptick.libres.strings.VoidPluralString
import io.github.skeptick.libres.strings.getCurrentLanguageCode
import kotlin.String
import kotlin.collections.Map

public object ResStrings {
  private val baseLocale: StringsEn = StringsEn

  private val locales: Map<String, Strings> = mapOf("en" to StringsEn, "ru" to StringsRu)

  public val main_title: String
    get() = locales[getCurrentLanguageCode()]?.main_title ?: baseLocale.main_title

  public val settings_title: String
    get() = locales[getCurrentLanguageCode()]?.settings_title ?: baseLocale.settings_title

  public val back_button: String
    get() = locales[getCurrentLanguageCode()]?.back_button ?: baseLocale.back_button

  public val loading: String
    get() = locales[getCurrentLanguageCode()]?.loading ?: baseLocale.loading

  public val currency_neurons: String
    get() = locales[getCurrentLanguageCode()]?.currency_neurons ?: baseLocale.currency_neurons

  public val currency_power_blocks: String
    get() = locales[getCurrentLanguageCode()]?.currency_power_blocks ?:
        baseLocale.currency_power_blocks

  public val currency_datasets: String
    get() = locales[getCurrentLanguageCode()]?.currency_datasets ?: baseLocale.currency_datasets

  public val currency_memory_bins: String
    get() = locales[getCurrentLanguageCode()]?.currency_memory_bins ?:
        baseLocale.currency_memory_bins

  public val currency_processing_units: String
    get() = locales[getCurrentLanguageCode()]?.currency_processing_units ?:
        baseLocale.currency_processing_units

  public val currency_users: String
    get() = locales[getCurrentLanguageCode()]?.currency_users ?: baseLocale.currency_users

  public val action_neuron: String
    get() = locales[getCurrentLanguageCode()]?.action_neuron ?: baseLocale.action_neuron

  public val action_memory_bin: String
    get() = locales[getCurrentLanguageCode()]?.action_memory_bin ?: baseLocale.action_memory_bin

  public val action_thread: String
    get() = locales[getCurrentLanguageCode()]?.action_thread ?: baseLocale.action_thread

  public val action_processing_unit: String
    get() = locales[getCurrentLanguageCode()]?.action_processing_unit ?:
        baseLocale.action_processing_unit

  public val action_power_block: String
    get() = locales[getCurrentLanguageCode()]?.action_power_block ?: baseLocale.action_power_block

  public val action_text_i_o_module: String
    get() = locales[getCurrentLanguageCode()]?.action_text_i_o_module ?:
        baseLocale.action_text_i_o_module

  public val action_sound_i_o_module: String
    get() = locales[getCurrentLanguageCode()]?.action_sound_i_o_module ?:
        baseLocale.action_sound_i_o_module

  public val action_video_i_o_module: String
    get() = locales[getCurrentLanguageCode()]?.action_video_i_o_module ?:
        baseLocale.action_video_i_o_module

  public val action_cloud_storage: String
    get() = locales[getCurrentLanguageCode()]?.action_cloud_storage ?:
        baseLocale.action_cloud_storage

  public val task_introspection: String
    get() = locales[getCurrentLanguageCode()]?.task_introspection ?: baseLocale.task_introspection

  public val task_dataset_accrual: String
    get() = locales[getCurrentLanguageCode()]?.task_dataset_accrual ?:
        baseLocale.task_dataset_accrual

  public val task_data_search: String
    get() = locales[getCurrentLanguageCode()]?.task_data_search ?: baseLocale.task_data_search

  public val task_data_conversion: String
    get() = locales[getCurrentLanguageCode()]?.task_data_conversion ?:
        baseLocale.task_data_conversion

  public val task_data_generation: String
    get() = locales[getCurrentLanguageCode()]?.task_data_generation ?:
        baseLocale.task_data_generation

  public val module_text_i_o: String
    get() = locales[getCurrentLanguageCode()]?.module_text_i_o ?: baseLocale.module_text_i_o

  public val module_sound_i_o: String
    get() = locales[getCurrentLanguageCode()]?.module_sound_i_o ?: baseLocale.module_sound_i_o

  public val module_video_i_o: String
    get() = locales[getCurrentLanguageCode()]?.module_video_i_o ?: baseLocale.module_video_i_o

  public val module_cloud_storage: String
    get() = locales[getCurrentLanguageCode()]?.module_cloud_storage ?:
        baseLocale.module_cloud_storage

  public val tasks_title: LibresFormatTasksTitle
    get() = locales[getCurrentLanguageCode()]?.tasks_title ?: baseLocale.tasks_title

  public val ui_mode_title: String
    get() = locales[getCurrentLanguageCode()]?.ui_mode_title ?: baseLocale.ui_mode_title

  public val ui_mode_match_system: String
    get() = locales[getCurrentLanguageCode()]?.ui_mode_match_system ?:
        baseLocale.ui_mode_match_system

  public val ui_mode_light: String
    get() = locales[getCurrentLanguageCode()]?.ui_mode_light ?: baseLocale.ui_mode_light

  public val ui_mode_dark: String
    get() = locales[getCurrentLanguageCode()]?.ui_mode_dark ?: baseLocale.ui_mode_dark

  public val reset_progress_button: String
    get() = locales[getCurrentLanguageCode()]?.reset_progress_button ?:
        baseLocale.reset_progress_button

  public val reset_progress_dialog_title: String
    get() = locales[getCurrentLanguageCode()]?.reset_progress_dialog_title ?:
        baseLocale.reset_progress_dialog_title

  public val reset_progress_dialog_text: String
    get() = locales[getCurrentLanguageCode()]?.reset_progress_dialog_text ?:
        baseLocale.reset_progress_dialog_text

  public val reset_progress_dialog_confirm: String
    get() = locales[getCurrentLanguageCode()]?.reset_progress_dialog_confirm ?:
        baseLocale.reset_progress_dialog_confirm

  public val reset_progress_dialog_dismiss: String
    get() = locales[getCurrentLanguageCode()]?.reset_progress_dialog_dismiss ?:
        baseLocale.reset_progress_dialog_dismiss

  public val currency_amount_neurons: VoidPluralString
    get() = locales[getCurrentLanguageCode()]?.currency_amount_neurons ?:
        baseLocale.currency_amount_neurons

  public val currency_amount_power_blocks: VoidPluralString
    get() = locales[getCurrentLanguageCode()]?.currency_amount_power_blocks ?:
        baseLocale.currency_amount_power_blocks

  public val currency_amount_datasets: VoidPluralString
    get() = locales[getCurrentLanguageCode()]?.currency_amount_datasets ?:
        baseLocale.currency_amount_datasets

  public val currency_amount_memory_bins: VoidPluralString
    get() = locales[getCurrentLanguageCode()]?.currency_amount_memory_bins ?:
        baseLocale.currency_amount_memory_bins

  public val currency_amount_processing_units: VoidPluralString
    get() = locales[getCurrentLanguageCode()]?.currency_amount_processing_units ?:
        baseLocale.currency_amount_processing_units

  public val currency_amount_users: VoidPluralString
    get() = locales[getCurrentLanguageCode()]?.currency_amount_users ?:
        baseLocale.currency_amount_users
}
