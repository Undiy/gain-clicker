package core


data class TasksState(
    val threadSlots: Int = 0,
    val tasks: List<TaskState> = Task.values().map { TaskState(it) },
    val taskThreads: Set<Task> = linkedSetOf()
) {
    fun addThreadSlot(): TasksState = copy(threadSlots = threadSlots + 1)

    fun toggleTaskThread(task: Task) = copy(
        taskThreads = if (task in taskThreads) {
            taskThreads - task
        } else {
            LinkedHashSet(taskThreads).apply {
                add(task)
                if (size > threadSlots) {
                    remove(first())
                }
            }
        }
    )
}

data class TaskState(
    val task: Task,
    val progress: Float = 0.0f
) {
    fun increaseProgress(inc: Float) = calculateGain(
        progress + inc,
        task.gain
    ).let { (remaining, gain) -> Pair(copy(progress = remaining), gain) }

    fun hasGainCapacity(state: GameState): Boolean {
        return when (this.task) {
            Task.DATASET_ACCRUAL -> state.deposit[Currency.MEMORY_BIN] > state.deposit[Currency.DATASET]
            else -> true
        }
    }
}

fun calculateGain(
    progress: Float,
    baseGain: List<CurrencyAmount>
): Pair<Float, List<CurrencyAmount>> {
    return progress.toInt().let { completed ->
        Pair(
            progress - completed,
            if (completed == 0) {
                listOf()
            } else {
                baseGain.map { it.copy(value = it.value * completed) }
            }
        )
    }
}

