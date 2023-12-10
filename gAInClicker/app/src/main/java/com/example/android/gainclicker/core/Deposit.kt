package com.example.android.gainclicker.core


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
