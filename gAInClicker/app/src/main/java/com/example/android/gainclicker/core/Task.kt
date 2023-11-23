package com.example.android.gainclicker.core

const val INTROSPECTION_NEURONS_REQUIRED = 100
const val DATASET_ACCRUAL_MEMORY_BINS_REQUIRED = 5
const val DATA_SEARCH_PROCESSING_UNITS_REQUIRED = 1
const val DATA_CONVERSION_PROCESSING_UNITS_REQUIRED = 100
const val DATA_GENERATION_DATASETS_REQUIRED = 1000
const val DATA_GENERATION_PROCESSING_UNITS_REQUIRED = 100


enum class Task(
    val requirement: List<CurrencyAmount> = listOf(),
    val ioModulesRequired: Int = 0
) {
    INTROSPECTION(
        CurrencyAmount(INTROSPECTION_NEURONS_REQUIRED, Currency.NEURON)
    ),
    DATASET_ACCRUAL(
        CurrencyAmount(DATASET_ACCRUAL_MEMORY_BINS_REQUIRED, Currency.MEMORY_BIN)
    ),
    DATA_SEARCH(
        CurrencyAmount(DATA_SEARCH_PROCESSING_UNITS_REQUIRED, Currency.PROCESSING_UNIT),
        1
    ),
    DATA_CONVERSION(
        CurrencyAmount(DATA_CONVERSION_PROCESSING_UNITS_REQUIRED, Currency.PROCESSING_UNIT),
        2
    ),
    DATA_GENERATION(
        listOf(
            CurrencyAmount(DATA_GENERATION_DATASETS_REQUIRED, Currency.DATASET),
            CurrencyAmount(DATA_GENERATION_PROCESSING_UNITS_REQUIRED, Currency.PROCESSING_UNIT)
        ),
        3
    );

    constructor(
        visibilityRequirement: CurrencyAmount,
        ioModulesRequired: Int = 0
    ) : this(listOf(visibilityRequirement), ioModulesRequired)

    fun isVisible(state: GameState): Boolean {
        return (true || state.ioModulesCount() >= ioModulesRequired)
                && state.deposit.hasAmount(*requirement.toTypedArray())
    }
}