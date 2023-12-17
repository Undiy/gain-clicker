package data.realm.schema

import core.Task
import core.TaskState
import core.TasksState
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.realmSetOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.ext.toRealmSet
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmSet

class RealmTasksState : EmbeddedRealmObject {

    var threadSlots: Int = 0
    var tasks: RealmList<RealmTaskState> = realmListOf()
    var taskThreads: RealmSet<String> = realmSetOf()
}

class RealmTaskState : EmbeddedRealmObject {

    var task: String = ""
    var progress: Float = 0.0f
}

fun RealmTaskState.toTaskState(): TaskState {
    return TaskState(
        task = Task.valueOf(task),
        progress = progress
    )
}

fun RealmTaskState.updateFrom(taskState: TaskState) {
    task = taskState.task.name
    progress = taskState.progress
}

fun TaskState.toRealmTaskState(): RealmTaskState {
    return RealmTaskState().also { it.updateFrom(this) }
}

fun RealmTasksState.toTasksState(): TasksState {
    return TasksState(
        threadSlots = threadSlots,
        tasks = tasks.map(RealmTaskState::toTaskState),
        taskThreads = taskThreads.map(Task::valueOf).toSet()
    )
}

fun RealmTasksState.updateFrom(tasksState: TasksState) {
    threadSlots = tasksState.threadSlots
    tasks.clear()
    tasks.addAll(tasksState.tasks.map(TaskState::toRealmTaskState).toRealmList())
    taskThreads.clear()
    taskThreads.addAll(tasksState.taskThreads.map(Task::name).toRealmSet())
}

fun TasksState.toRealmTasksState(): RealmTasksState {
    return RealmTasksState().also { it.updateFrom(this) }
}
