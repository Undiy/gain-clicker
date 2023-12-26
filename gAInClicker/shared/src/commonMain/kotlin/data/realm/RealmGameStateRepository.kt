package data.realm

import core.Deposit
import core.GameState
import core.Module
import core.TasksState
import core.VisibleFeatures
import data.GameStateRepository
import data.realm.schema.RealmDeposit
import data.realm.schema.RealmGameState
import data.realm.schema.RealmModule
import data.realm.schema.RealmTaskState
import data.realm.schema.RealmTasksState
import data.realm.schema.RealmVisibleFeatures
import data.realm.schema.toDeposit
import data.realm.schema.toGameState
import data.realm.schema.toModule
import data.realm.schema.toRealmDeposit
import data.realm.schema.toRealmGameState
import data.realm.schema.toRealmModule
import data.realm.schema.toRealmRealmVisibleFeatures
import data.realm.schema.toRealmTasksState
import data.realm.schema.toTasksState
import data.realm.schema.toVisibleFeatures
import data.realm.schema.updateFrom
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

    override fun close() {
        realm.close()
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
