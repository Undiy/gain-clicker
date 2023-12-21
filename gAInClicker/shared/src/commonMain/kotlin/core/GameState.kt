package core

import util.currentTimeMillis

const val PROGRESS_UPDATE_INTERVAL = 500
const val BASE_TASK_PROGRESS_RATE = 60_000.toFloat()

data class GameState(
    val deposit: Deposit = Deposit(),

    val modules: List<Module> = listOf(),

    val tasks: TasksState = TasksState(),

    val visibleFeatures: VisibleFeatures = VisibleFeatures(),

    var updatedAt: Long = currentTimeMillis()
) {
    fun ioModulesCount() = modules.count { it is IOModule }

    fun getCloudStorage() = modules.filterIsInstance<CloudStorage>().firstOrNull()

    private fun setCloudStorage(cloudStorage: CloudStorage) = modules.map {
        if (it is CloudStorage) cloudStorage else it
    }

    fun updateProgress(timestamp: Long): GameState {
        val multiplier = 1.0f + deposit[Currency.PROCESSING_UNIT] / 100.0f
        val progress = ((timestamp - updatedAt).coerceAtLeast(0)
            .toFloat() / BASE_TASK_PROGRESS_RATE)

        // Get cloud storage gain first since it is the only source of passive memory bins
        // It's still not full correct but should do for now
        return updateCloudStorageProgress(progress)
            .updateTasksProgress(progress * multiplier, timestamp)
    }

    private fun updateCloudStorageProgress(progress: Float): GameState {
        return getCloudStorage()?.let {
            it.increaseProgress(progress).let { (cloudStorage, cloudStorageGain) ->
                copy(
                    deposit = cloudStorageGain.fold(deposit) { dep, amount -> dep + amount },
                    modules = setCloudStorage(cloudStorage)
                )
            }
        } ?: this
    }

    private fun updateTasksProgress(progress: Float, timestamp: Long): GameState {
        return tasks.tasks.map { taskState ->
            if (taskState.task in tasks.taskThreads) {
                if (taskState.hasGainCapacity(this)) {
                    taskState.increaseProgress(progress)
                } else {
                    // set progress to 0.0f
                    taskState.increaseProgress(-taskState.progress)
                }
            } else {
                Pair(taskState, listOf())
            }
        }.unzip().let { (updatedTasks, tasksGain) ->

            copy(
                deposit = tasksGain
                    .flatten()
                    .fold(deposit) { dep, amount -> dep + amount },
                tasks = tasks.copy(
                    tasks = updatedTasks
                ),
                updatedAt = timestamp
            )
        }
    }

    fun isUpdatedRecently() = (currentTimeMillis() - updatedAt) < PROGRESS_UPDATE_INTERVAL * 4
}
