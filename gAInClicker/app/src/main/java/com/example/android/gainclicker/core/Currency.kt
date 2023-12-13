package com.example.android.gainclicker.core

const val NEURONS_LIMIT_POWER_BLOCKS_MODIFIER = 500

enum class Currency(val limit: CurrencyLimit = GeneralCurrencyLimit) {
    POWER_BLOCK,
    NEURON(
        DependencyCurrencyLimit(
            dependency = POWER_BLOCK,
            limitModifier = { (it + 1) * NEURONS_LIMIT_POWER_BLOCKS_MODIFIER }
        )
    ),
    MEMORY_BIN,
    DATASET(
        DependencyCurrencyLimit(
            dependency = MEMORY_BIN,
            limitModifier = { it }
        )
    ),
    PROCESSING_UNIT,
    USER
}

data class CurrencyAmount(val value: Int, val currency: Currency) {
    init {
        require(value >= 0)
    }
}

sealed interface CurrencyLimit
object GeneralCurrencyLimit : CurrencyLimit {
    const val limit: Int = 10_000
}
class DependencyCurrencyLimit(
    val dependency: Currency,
    val limitModifier : (Int) -> Int
) : CurrencyLimit