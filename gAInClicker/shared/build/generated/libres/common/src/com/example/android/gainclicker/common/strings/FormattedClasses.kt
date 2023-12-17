package com.example.android.gainclicker.common.strings

import io.github.skeptick.libres.strings.formatString
import io.github.skeptick.libres.strings.getPluralizedString
import kotlin.String

public class LibresFormatTasksTitle(
  private val `value`: String,
) {
  public fun format(tasks: String, threads: String): String = formatString(value,
      arrayOf(tasks,threads))
}
