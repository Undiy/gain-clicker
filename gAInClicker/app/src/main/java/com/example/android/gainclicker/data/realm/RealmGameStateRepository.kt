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
import com.example.android.gainclicker.data.realm.schema.toGameState
import com.example.android.gainclicker.data.realm.schema.toRealmDeposit
import com.example.android.gainclicker.data.realm.schema.toRealmGameState
import com.example.android.gainclicker.data.realm.schema.toRealmModule
import com.example.android.gainclicker.data.realm.schema.toRealmRealmVisibleFeatures
import com.example.android.gainclicker.data.realm.schema.toRealmTasksState
import com.example.android.gainclicker.data.realm.schema.updateFrom
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.flow.Flow
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

    private val gameStateQuery = realm.query(RealmGameState::class)

    override val gameState: Flow<GameState>
        get() = gameStateQuery.first().asFlow().mapNotNull {
            it.obj?.toGameState()
        }

    override suspend fun updateGameState(newGameState: GameState) {
        realm.write {
            findLatest(gameStateQuery.find().first())?.updateFrom(newGameState)
        }
    }

    override suspend fun updateDeposit(deposit: Deposit) {
        realm.write {
            findLatest(gameStateQuery.find().first())?.deposit = deposit.toRealmDeposit()
        }
    }

    override suspend fun updateModules(modules: List<Module>) {
        realm.write {
            findLatest(gameStateQuery.find().first())?.modules?.apply {
                modules.mapTo(
                    this,
                    Module::toRealmModule
                )
            }
        }
    }

    override suspend fun updateTasks(tasks: TasksState) {
        realm.write {
            findLatest(gameStateQuery.find().first())?.tasks = tasks.toRealmTasksState()
        }
    }

    override suspend fun updateVisibleFeatures(visibleFeatures: VisibleFeatures) {
        realm.write {
            findLatest(gameStateQuery.find().first())?.visibleFeatures = visibleFeatures
                .toRealmRealmVisibleFeatures()
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
