package core

const val CURRENCY_GAIN = 1

const val MEMORY_BIN_NEURONS_REQUIRED = 150
const val MEMORY_BIN_NEURONS_COST = 50

const val THREAD_FIRST_NEURONS_REQUIRED = 20
const val THREAD_FIRST_NEURONS_COST = 100

const val THREAD_SECOND_USERS_REQUIRED = 1
const val THREAD_SECOND_MEMORY_BINS_COST = 10

const val THREAD_THIRD_USERS_REQUIRED = 100
const val THREAD_THIRD_MEMORY_BINS_COST = 100

const val PROCESSING_UNIT_DATASETS_REQUIRED = 10
const val PROCESSING_UNIT_NEURONS_COST = 100

const val POWER_BLOCK_PROCESSING_UNITS_REQUIRED = 2
const val POWER_BLOCK_PROCESSING_UNITS_COST = 10

const val IO_MODULE_TEXT_DATASETS_REQUIRED = 5
const val IO_MODULE_TEXT_NEURONS_REQUIRED = 1000

const val IO_MODULE_SOUND_DATASETS_REQUIRED = 25
const val IO_MODULE_SOUND_NEURONS_COST = 5_000

const val IO_MODULE_VIDEO_DATASETS_REQUIRED = 100
const val IO_MODULE_VIDEO_NEURONS_COST = 10_000

const val CLOUD_STORAGE_USERS_REQUIRED = 100
const val CLOUD_STORAGE_NEURONS_COST = 1_000
const val CLOUD_PROCESSING_UNITS_COST = 100

const val MAX_TASK_THREAD_SLOTS = 3

sealed interface ClickActionGain
private data class CurrencyGain(val currency: Currency) : ClickActionGain
private data class ModuleGain(val module: Module): ClickActionGain
private object ThreadSlotGain : ClickActionGain


enum class ClickAction(
    private val visibilityRequirement: List<CurrencyAmount> = listOf(),
    val cost: List<CurrencyAmount> = listOf(),
    val gain: ClickActionGain
) {
    NEURON(
        gain = CurrencyGain(Currency.NEURON)
    ),
    MEMORY_BIN(
        CurrencyAmount(MEMORY_BIN_NEURONS_REQUIRED, Currency.NEURON),
        CurrencyAmount(MEMORY_BIN_NEURONS_COST, Currency.NEURON),
        CurrencyGain(Currency.MEMORY_BIN)
    ),
    THREAD_FIRST(
        CurrencyAmount(THREAD_FIRST_NEURONS_REQUIRED, Currency.NEURON),
        CurrencyAmount(THREAD_FIRST_NEURONS_COST, Currency.NEURON),
        ThreadSlotGain
    ),
    THREAD_SECOND(
        CurrencyAmount(THREAD_SECOND_USERS_REQUIRED, Currency.USER),
        CurrencyAmount(THREAD_SECOND_MEMORY_BINS_COST, Currency.MEMORY_BIN),
        ThreadSlotGain
    ),
    THREAD_THIRD(
        CurrencyAmount(THREAD_THIRD_USERS_REQUIRED, Currency.USER),
        CurrencyAmount(THREAD_THIRD_MEMORY_BINS_COST, Currency.MEMORY_BIN),
        ThreadSlotGain
    ),
    PROCESSING_UNIT(
        CurrencyAmount(PROCESSING_UNIT_DATASETS_REQUIRED, Currency.DATASET),
        CurrencyAmount(PROCESSING_UNIT_NEURONS_COST, Currency.NEURON),
        CurrencyGain(Currency.PROCESSING_UNIT)
    ),
    POWER_BLOCK(
        CurrencyAmount(POWER_BLOCK_PROCESSING_UNITS_REQUIRED, Currency.PROCESSING_UNIT),
        CurrencyAmount(POWER_BLOCK_PROCESSING_UNITS_COST, Currency.PROCESSING_UNIT),
        CurrencyGain(Currency.POWER_BLOCK)
    ),
    IO_MODULE_TEXT(
        CurrencyAmount(IO_MODULE_TEXT_DATASETS_REQUIRED, Currency.DATASET),
        CurrencyAmount(IO_MODULE_TEXT_NEURONS_REQUIRED, Currency.NEURON),
        ModuleGain(IOModule.IO_TEXT)
    ),
    IO_MODULE_SOUND(
        CurrencyAmount(IO_MODULE_SOUND_DATASETS_REQUIRED, Currency.DATASET),
        CurrencyAmount(IO_MODULE_SOUND_NEURONS_COST, Currency.NEURON),
        ModuleGain(IOModule.IO_SOUND)
    ),
    IO_MODULE_VIDEO(
        CurrencyAmount(IO_MODULE_VIDEO_DATASETS_REQUIRED, Currency.DATASET),
        CurrencyAmount(IO_MODULE_VIDEO_NEURONS_COST, Currency.NEURON),
        ModuleGain(IOModule.IO_VIDEO)
    ),
    CLOUD_STORAGE(
        listOf(CurrencyAmount(CLOUD_STORAGE_USERS_REQUIRED, Currency.PROCESSING_UNIT)),
        listOf(
            CurrencyAmount(CLOUD_STORAGE_NEURONS_COST, Currency.NEURON),
            CurrencyAmount(CLOUD_PROCESSING_UNITS_COST, Currency.PROCESSING_UNIT)
        ),
        ModuleGain(CloudStorage())
    );

    constructor(
        visibilityRequirement: CurrencyAmount,
        cost: CurrencyAmount,
        gain: ClickActionGain,
    ) : this(listOf(visibilityRequirement), listOf(cost), gain)

    private fun isNotLimited(state: GameState): Boolean {
        return when (gain) {
            is CurrencyGain -> true
            is ModuleGain -> when (gain.module) {
                is IOModule -> gain.module !in state.modules
                is CloudStorage -> state.modules.none { it is CloudStorage }
            }
            ThreadSlotGain -> {
                state.tasks.threadSlots < when (this) {
                    THREAD_FIRST -> 1
                    THREAD_SECOND -> 2
                    THREAD_THIRD -> 3
                    else -> throw RuntimeException("ThreadSlotGain in click action $this")
                }
            }
        }
    }

    fun isVisible(state: GameState): Boolean {
        return isNotLimited(state) && (this in state.visibleFeatures.actions
                || state.deposit.hasAmount(*visibilityRequirement.toTypedArray()))
    }

    fun isAcquirable(state: GameState): Boolean {
        return isNotLimited(state)
                && state.deposit.hasAmount(*cost.toTypedArray())
                && (gain !is CurrencyGain || !state.deposit.isCurrencyFull(gain.currency))
    }

    fun acquire(state: GameState): GameState {
        // withdraw cost
        val deposit = cost.fold(state.deposit) { deposit, amount -> deposit - amount }

        return when (gain) {
            is CurrencyGain -> state.copy(
                deposit = deposit + CurrencyAmount(CURRENCY_GAIN, gain.currency)
            )
            is ModuleGain -> state.copy(
                deposit = deposit, modules = state.modules + gain.module
            )
            is ThreadSlotGain -> state.copy(
                deposit = deposit, tasks = state.tasks.addThreadSlot()
            )
        }
    }
}
