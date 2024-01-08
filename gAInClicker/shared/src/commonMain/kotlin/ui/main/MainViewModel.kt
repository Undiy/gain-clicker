package ui.main

import core.ClickAction
import core.CloudStorage
import core.GameState
import core.IOModule
import core.Module
import core.PROGRESS_UPDATE_INTERVAL
import core.Task
import data.GameStateRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import util.currentTimeMillis
import kotlin.concurrent.Volatile
import kotlin.time.Duration.Companion.milliseconds

const val SAVE_STATE_INTERVAL = 10_000L

class MainViewModel(
    private val gameStateRepository: GameStateRepository
) : ViewModel() {

    private val _gameState = MutableStateFlow(GameState(updatedAt = 0L))

    @Volatile
    private var loadedAt: Long? = null

    val gameState: StateFlow<GameState>
        get() = _gameState

    private val timer = flow {
        while (true) {
            emit(currentTimeMillis())
            delay(PROGRESS_UPDATE_INTERVAL.milliseconds)
        }
    }

    private val startStopLock = Mutex()

    private var loader: Job? = null

    private val stateLoaded: Boolean
        get() = loadedAt != null
    private var updater: Job? = null

    private suspend fun startUpdater() {
        if (updater == null) {
            updater = viewModelScope.launch(Dispatchers.Default) {
                timer.collect { timestamp ->
                    if (stateLoaded) {
                        _gameState.update {
                            it.updateProgress(timestamp)
                        }
                        if (_gameState.value.updatedAt - loadedAt!! >= SAVE_STATE_INTERVAL) {
                            loadedAt = null
                            Napier.i("Saving state...")
                            gameStateRepository.updateGameState { _gameState.value }
                            Napier.i("State saved with ts ${_gameState.value.updatedAt}")
                        }
                    } else {
                        Napier.i("Skip update: not loaded")
                    }
                }
            }
        } else {
            Napier.w { "Updater already started!" }
        }
    }

    private suspend fun stopUpdater() {
        updater?.cancelAndJoin()
        updater = null
    }


    private suspend fun startLoader() {
        if (loader == null) {
            loader = viewModelScope.launch {
                gameStateRepository.gameState.collect { loadedGameState ->
                    loadedAt = _gameState.updateAndGet {
                        loadedGameState
                    }.updatedAt
                    Napier.i("State loaded with ts $loadedAt")
                }
            }
        } else {
            Napier.w { "Loader already started!" }
        }
    }

    private suspend fun stopLoader() {
        loader?.cancelAndJoin()
        loader = null
    }

    fun onStart() {
        Napier.i("onStart")
        viewModelScope.launch {
            startStopLock.withLock {
                startLoader()
                Napier.i("onStart: started Loader")
                startUpdater()
                Napier.i("onStart: started Updater")

            }
        }
    }

    fun onStop() {
        Napier.i("onStop")
        viewModelScope.launch {
            startStopLock.withLock {
                stopUpdater()
                Napier.i("onStop: stopped updater")
                stopLoader()
                Napier.i("onStop: stopped loader")
                gameStateRepository.updateGameState { _gameState.value }
                Napier.i("onStop: saved state")
            }
        }
    }

    fun isActionVisible(action: ClickAction): Boolean {
        return action.isVisible(gameState.value).also {visible ->
            if (visible && action !in gameState.value.visibleFeatures.actions) {
                _gameState.update {
                    it.copy(
                        visibleFeatures = it.visibleFeatures.copy(
                            actions = it.visibleFeatures.actions + action
                        )
                    )
                }
            }
        }
    }

    fun isActionEnabled(action: ClickAction) = action.isAcquirable(gameState.value)

    fun onActionClick(action: ClickAction) {
        if (stateLoaded) {
            _gameState.update {
                if (action.isAcquirable(it)) {
                    action.acquire(it)
                } else {
                    it
                }
            }
        } else {
            Napier.w("Skipping action click (state not loaded): $action")
        }
    }

    fun isTasksViewVisible(): Boolean {
        return gameState.value.tasks.threadSlots > 0
    }

    fun isTaskVisible(task: Task): Boolean {
        return task.isVisible(gameState.value).also { visible ->
            gameState.value.visibleFeatures.let { visibleFeatures ->
                if (visible && task !in visibleFeatures.tasks) {
                    _gameState.update {
                        it.copy(
                            visibleFeatures = it.visibleFeatures.copy(
                                tasks = it.visibleFeatures.tasks + task
                            )
                        )
                    }
                }
            }
        }
    }

    fun onTaskClick(task: Task) {
        if (stateLoaded) {
            viewModelScope.launch {
                _gameState.update {
                    it.copy(
                        tasks = it.tasks.toggleTaskThread(task)
                    )
                }
            }
        } else {
            Napier.w("Skipping task click (state not loaded): $task")
        }
    }

    fun isModuleVisible(module: Module): Boolean {
        return when (module) {
            IOModule.IO_TEXT -> ClickAction.IO_MODULE_TEXT
            IOModule.IO_SOUND -> ClickAction.IO_MODULE_SOUND
            IOModule.IO_VIDEO -> ClickAction.IO_MODULE_VIDEO
            is CloudStorage -> ClickAction.CLOUD_STORAGE
        } in gameState.value.visibleFeatures.actions
    }

    fun isModuleEnabled(module: Module) = module in gameState.value.modules
}
