package com.example.android.gainclicker.data.realm.schema

import com.example.android.gainclicker.core.Deposit
import com.example.android.gainclicker.core.GameState
import com.example.android.gainclicker.core.Module
import com.example.android.gainclicker.core.TasksState
import com.example.android.gainclicker.core.VisibleFeatures
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import org.mongodb.kbson.ObjectId

class RealmGameState : RealmObject {

    var _id: ObjectId = ObjectId()

    var deposit: RealmDeposit? = null

    var modules: RealmList<RealmModule> = realmListOf()

    var tasks: RealmTasksState? = null

    var visibleFeatures: RealmVisibleFeatures? = null

    var updatedAt: Long = System.currentTimeMillis()
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
