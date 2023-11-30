package com.example.android.gainclicker.core

const val LIMIT_GENERAL = 10_000

enum class Currency {
    NEURON,
    DATASET,
    MEMORY_BIN,
    PROCESSING_UNIT,
    USER
}

data class CurrencyAmount(val value: Int, val currency: Currency) {
    init {
        require(value >= 0)
    }
}