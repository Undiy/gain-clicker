package com.example.android.gainclicker.core

import android.util.Log
import com.example.android.gainclicker.ui.PROGRESS_UPDATE_INTERVAL

const val BASE_TASK_PROGRESS_RATE = 60_000.toFloat()
data class GameState(
    val deposit: Deposit = Deposit(),

    val modules: List<Module> = listOf(),

    val tasks: TasksState = TasksState(),

    val visibleFeatures: VisibleFeatures = VisibleFeatures(),

    var updatedAt: Long = System.currentTimeMillis()
) {
    fun ioModulesCount() = modules.count { it is IOModule }

    fun getCloudStorage() = modules.filterIsInstance<CloudStorage>().firstOrNull()

    private fun setCloudStorage(cloudStorage: CloudStorage) = modules.map {
        if (it is CloudStorage) cloudStorage else it
    }

    fun updateProgress(timestamp: Long): GameState {
        val datasetMultiplier = 1.0f + deposit[Currency.PROCESSING_UNIT] / 100.0f
        val progress = ((timestamp - updatedAt).coerceAtLeast(0)
            .toFloat() / BASE_TASK_PROGRESS_RATE)

        Log.i("PROGRESS", "Time delta: ${timestamp - updatedAt}/$PROGRESS_UPDATE_INTERVAL")
        Log.i("PROGRESS", "Updated progress: $progress $datasetMultiplier")

        val (updatedTasks, tasksGain) = tasks.tasks.map { taskState ->
            if (taskState.task in tasks.taskThreads) {
                if (taskState.hasGainCapacity(this)) {
                    taskState.increaseProgress(progress * datasetMultiplier)
                } else {
                    // set progress to 0.0f
                    taskState.increaseProgress(-taskState.progress)
                }
            } else {
                Pair(taskState, listOf())
            }
        }.unzip()

        val (cloudStorage, cloudStorageGain) = getCloudStorage().let {
            it?.increaseProgress(progress) ?: (null to listOf())
        }

        return copy(
            deposit = tasksGain
                .flatten()
                .plus(cloudStorageGain)
                .fold(deposit) { dep, amount -> dep + amount},
            tasks = tasks.copy(
                tasks = updatedTasks
            ),
            modules = if (cloudStorage == null) {
                modules
            } else {
                setCloudStorage(cloudStorage)
            },
            updatedAt = timestamp
        )
    }

    fun isUpdatedRecently() = (System.currentTimeMillis() - updatedAt) < PROGRESS_UPDATE_INTERVAL * 4
}
