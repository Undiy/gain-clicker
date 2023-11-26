package com.example.android.gainclicker.core


const val CLOUD_STORAGE_CURRENCY_GAIN = 1
sealed interface Module

enum class IOModule : Module { IO_TEXT, IO_SOUND, IO_VIDEO }
data class CloudStorage(
    val progress: Float = 0.0f
) : Module {

    companion object {
        val gain = listOf(CurrencyAmount(CLOUD_STORAGE_CURRENCY_GAIN, Currency.MEMORY_BIN))

    }
    fun increaseProgress(inc: Float) = calculateGain(
        progress + inc,
        gain
    ).let { (remaining, gain) -> copy(progress = remaining) to gain }
}
