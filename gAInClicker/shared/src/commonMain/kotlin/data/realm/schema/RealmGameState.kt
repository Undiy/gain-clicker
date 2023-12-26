package data.realm.schema

import core.Deposit
import core.GameState
import core.Module
import core.TasksState
import core.VisibleFeatures
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import org.mongodb.kbson.ObjectId
import util.currentTimeMillis

class RealmGameState : RealmObject {

    var _id: ObjectId = ObjectId()

    var deposit: RealmDeposit? = null

    var modules: RealmList<RealmModule> = realmListOf()

    var tasks: RealmTasksState? = null

    var visibleFeatures: RealmVisibleFeatures? = null

    var updatedAt: Long = currentTimeMillis()
}

fun RealmGameState.toGameState(): GameState {
    return GameState(
        deposit = deposit?.toDeposit() ?: Deposit(),
        modules = modules.map(RealmModule::toModule),
        tasks = tasks?.toTasksState() ?: TasksState(),
        visibleFeatures = visibleFeatures?.toVisibleFeatures() ?: VisibleFeatures(),
        updatedAt = updatedAt
    )
}

fun RealmGameState.updateFrom(gameState: GameState) {

    if (deposit == null) deposit = RealmDeposit()
    deposit!!.updateFrom(gameState.deposit)

    modules.clear()
    gameState.modules.mapTo(modules, Module::toRealmModule)

    if (tasks == null) tasks = RealmTasksState()
    tasks!!.updateFrom(gameState.tasks)

    if (visibleFeatures == null) visibleFeatures = RealmVisibleFeatures()
    visibleFeatures!!.updateFrom(gameState.visibleFeatures)

    updatedAt = gameState.updatedAt
}

fun GameState.toRealmGameState(): RealmGameState {
    return RealmGameState().also { it.updateFrom(this) }
}
