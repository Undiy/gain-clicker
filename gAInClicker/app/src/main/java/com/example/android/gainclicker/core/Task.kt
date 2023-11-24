package com.example.android.gainclicker.core

const val INTROSPECTION_NEURONS_REQUIRED = 100
const val DATASET_ACCRUAL_MEMORY_BINS_REQUIRED = 5
const val DATA_SEARCH_PROCESSING_UNITS_REQUIRED = 1
const val DATA_CONVERSION_PROCESSING_UNITS_REQUIRED = 100
const val DATA_GENERATION_DATASETS_REQUIRED = 1000
const val DATA_GENERATION_PROCESSING_UNITS_REQUIRED = 100

const val TASK_CURRENCY_GAIN = 1

enum class Task(
    val requirement: List<CurrencyAmount>,
    val ioModulesRequired: Int = 0,
    val gain: List<CurrencyAmount>
) {
    INTROSPECTION(
        requirement = CurrencyAmount(INTROSPECTION_NEURONS_REQUIRED, Currency.NEURON),
        gain = CurrencyAmount(TASK_CURRENCY_GAIN, Currency.NEURON)
    ),
    DATASET_ACCRUAL(
        requirement = CurrencyAmount(DATASET_ACCRUAL_MEMORY_BINS_REQUIRED, Currency.MEMORY_BIN),
        gain = CurrencyAmount(TASK_CURRENCY_GAIN, Currency.DATASET)
    ),
    DATA_SEARCH(
        requirement = CurrencyAmount(DATA_SEARCH_PROCESSING_UNITS_REQUIRED, Currency.PROCESSING_UNIT),
        ioModulesRequired = 1,
        gain = CurrencyAmount(TASK_CURRENCY_GAIN, Currency.USER)
    ),
    DATA_CONVERSION(
        requirement = listOf(CurrencyAmount(DATA_CONVERSION_PROCESSING_UNITS_REQUIRED, Currency.PROCESSING_UNIT)),
        ioModulesRequired = 2,
        gain = listOf(
            CurrencyAmount(TASK_CURRENCY_GAIN, Currency.USER),
            CurrencyAmount(TASK_CURRENCY_GAIN, Currency.DATASET)
        )
    ),
    DATA_GENERATION(
        requirement = listOf(
            CurrencyAmount(DATA_GENERATION_DATASETS_REQUIRED, Currency.DATASET),
            CurrencyAmount(DATA_GENERATION_PROCESSING_UNITS_REQUIRED, Currency.PROCESSING_UNIT)
        ),
        ioModulesRequired = 3,
        gain = listOf(
            CurrencyAmount(TASK_CURRENCY_GAIN, Currency.USER),
            CurrencyAmount(TASK_CURRENCY_GAIN, Currency.DATASET),
            CurrencyAmount(TASK_CURRENCY_GAIN, Currency.NEURON)
        )
    );

    constructor(
        requirement: CurrencyAmount,
        ioModulesRequired: Int = 0,
        gain: CurrencyAmount
    ) : this(listOf(requirement), ioModulesRequired, listOf(gain))

    fun isVisible(state: GameState): Boolean {
        return this in state.visibleFeatures.tasks || (
                state.ioModulesCount() >= ioModulesRequired
                && state.deposit.hasAmount(*requirement.toTypedArray()))
    }
}