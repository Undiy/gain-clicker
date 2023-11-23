package com.example.android.gainclicker.core

data class GameState(
    val deposit: Deposit = Deposit(),

    val modules: Set<Module> = setOf(),

    val taskThreads: TaskThreadsState = TaskThreadsState()
)

data class Deposit(
    private val accounts: Map<Currency, Int> = mapOf()
) {
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
    val tasks: Task,
    val progress: Int = 0
)

data class TaskThreadsState(
    val threadSlots: Int = 0,
    val tasks: List<TaskState> = listOf(),
    val taskThread: Set<TaskState> = linkedSetOf()
) {
    fun addThreadSlot(): TaskThreadsState = copy(threadSlots = threadSlots + 1)
}
