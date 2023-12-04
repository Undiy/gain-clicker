package com.example.android.gainclicker.core

import android.util.Log
import com.example.android.gainclicker.ui.PROGRESS_UPDATE_INTERVAL

const val BASE_TASK_PROGRESS_RATE = 60_000.toFloat()
data class GameState(
    val deposit: Deposit = Deposit(),

    val modules: List<Module> = listOf(),

    val tasks: TasksState = TasksState(),

    val visibleFeatures: VisibleFeatures = VisibleFeatures(),

    var updatedAt: Long = System.currentTimeMillis()
) {
    fun ioModulesCount() = modules.count { it is IOModule }

    fun getCloudStorage() = modules.filterIsInstance<CloudStorage>().firstOrNull()

    private fun setCloudStorage(cloudStorage: CloudStorage) = modules.map {
        if (it is CloudStorage) cloudStorage else it
    }

    fun updateProgress(timestamp: Long): GameState {
        val datasetMultiplier = 1.0f + deposit[Currency.PROCESSING_UNIT] / 100.0f
        val progress = ((timestamp - updatedAt).coerceAtLeast(0)
            .toFloat() / BASE_TASK_PROGRESS_RATE)

        Log.i("PROGRESS", "Time delta: ${timestamp - updatedAt}/$PROGRESS_UPDATE_INTERVAL")
        Log.i("PROGRESS", "Updated progress: $progress $datasetMultiplier")

        val (updatedTasks, tasksGain) = tasks.tasks.map { taskState ->
            if (taskState.task in tasks.taskThreads) {
                if (taskState.hasGainCapacity(this)) {
                    taskState.increaseProgress(progress * datasetMultiplier)
                } else {
                    // set progress to 0.0f
                    taskState.increaseProgress(-taskState.progress)
                }
            } else {
                Pair(taskState, listOf())
            }
        }.unzip()

        val (cloudStorage, cloudStorageGain) = getCloudStorage().let {
            it?.increaseProgress(progress) ?: (null to listOf())
        }

        return copy(
            deposit = tasksGain
                .flatten()
                .plus(cloudStorageGain)
                .fold(deposit) { dep, amount -> dep + amount},
            tasks = tasks.copy(
                tasks = updatedTasks
            ),
            modules = if (cloudStorage == null) {
                modules
            } else {
                setCloudStorage(cloudStorage)
            },
            updatedAt = timestamp
        )
    }

    fun isUpdatedRecently() = (System.currentTimeMillis() - updatedAt) < PROGRESS_UPDATE_INTERVAL * 4
}

data class Deposit(
    val accounts: Map<Currency, Int> = mapOf()
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

    private fun getCurrencyLimit(currency: Currency): Int {
        return when(currency) {
            Currency.DATASET -> this[Currency.MEMORY_BIN]
            else -> LIMIT_GENERAL
        }
    }

    operator fun plus(amount: CurrencyAmount) = copy(
        accounts = (accounts + (amount.currency to this[amount.currency] + amount.value))
            .mapValues { (currency, value) -> value.coerceAtMost(getCurrencyLimit(currency)) }
    )

    operator fun minus(amount: CurrencyAmount): Deposit {
        require(hasAmount(amount)) { "Insufficient amount: $amount in deposit $this" }
        // can't use plus, because amount value must be non-negative
        return copy(
            accounts = accounts + (amount.currency to this[amount.currency] - amount.value)
        )
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

data class VisibleFeatures(
    val tasks: Set<Task> = setOf(),
    val actions: Set<ClickAction> = setOf()
)
