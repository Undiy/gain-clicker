package com.example.android.gainclicker.data.realm

import android.util.Log
import com.example.android.gainclicker.core.Deposit
import com.example.android.gainclicker.core.GameState
import com.example.android.gainclicker.core.Module
import com.example.android.gainclicker.core.TasksState
import com.example.android.gainclicker.core.VisibleFeatures
import com.example.android.gainclicker.data.GameStateRepository
import com.example.android.gainclicker.data.realm.schema.RealmDeposit
import com.example.android.gainclicker.data.realm.schema.RealmGameState
import com.example.android.gainclicker.data.realm.schema.RealmModule
import com.example.android.gainclicker.data.realm.schema.RealmTaskState
import com.example.android.gainclicker.data.realm.schema.RealmTasksState
import com.example.android.gainclicker.data.realm.schema.RealmVisibleFeatures
import com.example.android.gainclicker.data.realm.schema.toDeposit
import com.example.android.gainclicker.data.realm.schema.toGameState
import com.example.android.gainclicker.data.realm.schema.toModule
import com.example.android.gainclicker.data.realm.schema.toRealmDeposit
import com.example.android.gainclicker.data.realm.schema.toRealmGameState
import com.example.android.gainclicker.data.realm.schema.toRealmModule
import com.example.android.gainclicker.data.realm.schema.toRealmRealmVisibleFeatures
import com.example.android.gainclicker.data.realm.schema.toRealmTasksState
import com.example.android.gainclicker.data.realm.schema.toTasksState
import com.example.android.gainclicker.data.realm.schema.toVisibleFeatures
import com.example.android.gainclicker.data.realm.schema.updateFrom
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull

class RealmGameStateRepository : GameStateRepository {

    private val realm by lazy {
        val config = RealmConfiguration.Builder(
            schema = schema
        )
            .initialData {
                copyToRealm(GameState().toRealmGameState())
            }
            .build()
        Realm.open(config)
    }

    private val gameStateQuery
        get() = realm.query(RealmGameState::class)

    override val gameState: Flow<GameState>
        get() = gameStateQuery.first().asFlow()
            .mapNotNull {
                it.obj?.toGameState()
            }
            .flowOn(Dispatchers.Default)

    override suspend fun updateGameState(updater: (GameState) -> GameState) {
        realm.write {
            findLatest(gameStateQuery.find().first())?.apply {
                updateFrom(updater(this.toGameState()))
            }
        }
    }

    override suspend fun updateDeposit(updater: (Deposit) -> Deposit) {
        realm.write {
            findLatest(gameStateQuery.find().first())?.apply {
                deposit = updater(deposit?.toDeposit() ?: Deposit()).toRealmDeposit()
            }
        }
    }

    override suspend fun updateModules(updater: (List<Module>) -> List<Module>) {
        realm.write {
            findLatest(gameStateQuery.find().first())?.apply {
                modules = updater(modules.map(RealmModule::toModule))
                    .map(Module::toRealmModule).toRealmList()
            }
        }
    }

    override suspend fun updateTasks(updater: (TasksState) -> TasksState) {
        realm.write {
            findLatest(gameStateQuery.find().first())?.apply {
                tasks = updater(tasks?.toTasksState() ?: TasksState()).toRealmTasksState()
            }
        }
    }

    override suspend fun updateVisibleFeatures(updater: (VisibleFeatures) -> VisibleFeatures) {
        realm.write {
            findLatest(gameStateQuery.find().first())?.apply {
                visibleFeatures = updater(visibleFeatures?.toVisibleFeatures() ?: VisibleFeatures())
                    .toRealmRealmVisibleFeatures()
            }
        }
    }

    companion object {
        val schema = setOf(
            RealmGameState::class,

            RealmDeposit::class,

            RealmModule::class,

            RealmTasksState::class,
            RealmTaskState::class,

            RealmVisibleFeatures::class
        )
    }
}
