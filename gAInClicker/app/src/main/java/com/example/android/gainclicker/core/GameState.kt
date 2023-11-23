package com.example.android.gainclicker.core

import android.util.Log

const val BASE_TASK_PROGRESS_RATE = 10_000.toFloat()
data class GameState(
    val deposit: Deposit = Deposit(),

    val modules: Set<Module> = setOf(),

    val tasks: TaskThreadsState = TaskThreadsState()
) {
    fun ioModulesCount() = modules.count { it.isIo }

    fun updateTasks(timestamp: Long): GameState {
        val datasetMultiplier = 1.0f + deposit[Currency.DATASET] / 100.0f
        val progress = ((timestamp - tasks.updatedAt).coerceAtLeast(0)
            .toFloat() / BASE_TASK_PROGRESS_RATE) * datasetMultiplier

        Log.i("PROGRESS", "Updated progress: $progress $datasetMultiplier")

        val (updatedTasks, gain) = tasks.tasks.map { taskState ->
            if (taskState.task in tasks.taskThreads) {
                taskState.increaseProgress(progress)
            } else {
                Pair(taskState, listOf())
            }
        }.unzip()

        return copy(
            deposit = gain.flatten().fold(deposit) { dep, amount -> dep + amount},
            tasks = tasks.copy(
                tasks = updatedTasks,
                updatedAt = timestamp
            )
        )
    }
}

data class Deposit(
    private val accounts: Map<Currency, Int> = mapOf()
) {
    constructor(
        neurons: Int = 0,
        datasets: Int = 0,
        memoryBins: Int = 0,
        processingUnits: Int = 0,
        users: Int = 0
    ) : this(
        mapOf(
            Currency.NEURON to neurons,
            Currency.DATASET to datasets,
            Currency.MEMORY_BIN to memoryBins,
            Currency.PROCESSING_UNIT to processingUnits,
            Currency.USER to users
        )
    )
    operator fun get(currency: Currency) = accounts[currency] ?: 0

    private fun hasAmount(amount: CurrencyAmount) =
        this[amount.currency] >= amount.value

    fun hasAmount(vararg amount: CurrencyAmount): Boolean = amount.all(::hasAmount)

    operator fun plus(amount: CurrencyAmount) = copy(
        accounts = accounts + (amount.currency to this[amount.currency] + amount.value)
    )

    operator fun minus(amount: CurrencyAmount): Deposit {
        require(hasAmount(amount)) { "Insufficient amount: $amount in deposit $this" }
        // can't use plus, because amount value must be non-negative
        return copy(
            accounts = accounts + (amount.currency to this[amount.currency] - amount.value)
        )
    }
}

data class TaskState(
    val task: Task,
    val progress: Float = 0.0f
) {
    fun increaseProgress(inc: Float): Pair<TaskState, List<CurrencyAmount>> {
        val total = progress + inc
        val completed = total.toInt()

        return Pair(
            copy(progress = total - completed),
            if (completed == 0) {
                listOf()
            } else {
                task.gain.map { it.copy(value = it.value * completed) }
            }
        )
    }
}

data class TaskThreadsState(
    val threadSlots: Int = 0,
    val tasks: List<TaskState> = Task.values().map { TaskState(it) },
    val taskThreads: Set<Task> = linkedSetOf(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun addThreadSlot(): TaskThreadsState = copy(threadSlots = threadSlots + 1)

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
