package com.example.android.gainclicker.common.strings

import io.github.skeptick.libres.strings.PluralForms
import io.github.skeptick.libres.strings.VoidPluralString
import io.github.skeptick.libres.strings.getCurrentLanguageCode
import kotlin.String

public object StringsRu : Strings {
  override val main_title: String? = null

  override val settings_title: String = "Настройки"

  override val back_button: String = "Назад"

  override val loading: String = "ЗАГРУЗКА"

  override val currency_neurons: String = "Нейроны"

  override val currency_power_blocks: String = "Энергоблоки"

  override val currency_datasets: String = "Датасеты"

  override val currency_memory_bins: String = "Ячейки Памяти"

  override val currency_processing_units: String = "Процессоры"

  override val currency_users: String = "Пользователи"

  override val action_neuron: String = "Добавить нейрон"

  override val action_memory_bin: String = "Выделить ячейку памяти"

  override val action_thread: String = "Создать поток задачи"

  override val action_processing_unit: String = "Добавить процессор"

  override val action_power_block: String = "Добавить энергоблок"

  override val action_text_i_o_module: String = "Установить текстовый I/O модуль"

  override val action_sound_i_o_module: String = "Установить аудио I/O модуль"

  override val action_video_i_o_module: String = "Установить видео I/O модуль"

  override val action_cloud_storage: String = "Установить облачное хранилище"

  override val task_introspection: String = "Самоанализ"

  override val task_dataset_accrual: String = "Получение датасетов"

  override val task_data_search: String = "Сбор данных"

  override val task_data_conversion: String = "Преобразование данных"

  override val task_data_generation: String = "Генерация данных"

  override val module_text_i_o: String = "Текстовый I/O"

  override val module_sound_i_o: String = "Аудио I/O"

  override val module_video_i_o: String = "Видео I/O"

  override val module_cloud_storage: String = "Облачное Хранилище"

  override val tasks_title: LibresFormatTasksTitle =
      LibresFormatTasksTitle("Задачи %1${'$'}s/%2${'$'}s")

  override val ui_mode_title: String = "Тема:"

  override val ui_mode_match_system: String = "Системная"

  override val ui_mode_light: String = "Светлая"

  override val ui_mode_dark: String = "Тёмная"

  override val reset_progress_button: String = "Сбросить прогресс"

  override val reset_progress_dialog_title: String = "Вы уверены, что хотите сбросить прогресс?"

  override val reset_progress_dialog_text: String = "Это действие не может быть отменено"

  override val reset_progress_dialog_confirm: String = "Сбросить"

  override val reset_progress_dialog_dismiss: String = "Отмена"

  override val currency_amount_neurons: VoidPluralString = VoidPluralString(PluralForms(one =
      "нейрон", few = "нейрона", many = "нейронов", ), "ru")

  override val currency_amount_power_blocks: VoidPluralString = VoidPluralString(PluralForms(one =
      "энергоблок", few = "энергоблока", many = "энергоблоков", ), "ru")

  override val currency_amount_datasets: VoidPluralString = VoidPluralString(PluralForms(one =
      "датасет", few = "датасета", many = "датасетов", ), "ru")

  override val currency_amount_memory_bins: VoidPluralString = VoidPluralString(PluralForms(one =
      "ячейка памяти", few = "ячейки памяти", many = "ячеек памяти", ), "ru")

  override val currency_amount_processing_units: VoidPluralString = VoidPluralString(PluralForms(one
      = "процессор", few = "процессора", many = "процессоров", ), "ru")

  override val currency_amount_users: VoidPluralString = VoidPluralString(PluralForms(one =
      "пользователь", few = "пользователя", many = "пользователей", ), "ru")
}
