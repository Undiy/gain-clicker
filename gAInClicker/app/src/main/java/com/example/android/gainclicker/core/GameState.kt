package com.example.android.gainclicker.core

data class GameState(
    val deposit: Deposit = Deposit(),

    val modules: Set<Module> = setOf(),

    val tasks: TaskThreadsState = TaskThreadsState()
) {
    fun ioModulesCount() = modules.count { it.isIo }
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
)

data class TaskThreadsState(
    val threadSlots: Int = 0,
    val tasks: List<TaskState> = Task.values().map { TaskState(it) },
    val taskThreads: Set<Task> = linkedSetOf()
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
