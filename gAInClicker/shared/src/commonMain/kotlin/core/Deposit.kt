package core


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

    operator fun plus(amount: CurrencyAmount) = copy(
        accounts = (accounts + (amount.currency to this[amount.currency] + amount.value))
            .applyCurrencyLimits()
    )

    operator fun minus(amount: CurrencyAmount): Deposit {
        require(hasAmount(amount)) { "Insufficient amount: $amount in deposit $this" }
        // can't use plus, because amount value must be non-negative
        return copy(
            accounts = (accounts + (amount.currency to this[amount.currency] - amount.value))
                .applyCurrencyLimits()
        )
    }

    fun getCurrencyLimit(currency: Currency) = accounts.getCurrencyLimit(currency)

    fun isCurrencyFull(currency: Currency) = this[currency] == accounts.getCurrencyLimit(currency)

    companion object {
        private fun Map<Currency, Int>.applyCurrencyLimits() = this
            .mapValues { (currency, value) ->
                value.coerceAtMost(getCurrencyLimit(currency))
            }

        private fun Map<Currency, Int>.getCurrencyLimit(
            currency: Currency
        ) = when (currency.limit) {
            GeneralCurrencyLimit -> GeneralCurrencyLimit.limit
            is DependencyCurrencyLimit -> (this[currency.limit.dependency] ?: 0)
                .let(currency.limit.limitModifier)
        }
    }
}
