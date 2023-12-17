package core

import core.ClickAction
import core.Task


data class VisibleFeatures(
    val tasks: Set<Task> = setOf(),
    val actions: Set<ClickAction> = setOf()
)
