package ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.GameState
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.lifecycle.Lifecycle
import moe.tlaster.precompose.lifecycle.LifecycleObserver
import moe.tlaster.precompose.lifecycle.LocalLifecycleOwner
import ui.elements.ActionList
import ui.elements.GameInfo
import ui.elements.TaskList
import undiy.games.gainclicker.common.Res


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel(MainViewModel::class),
    onDispose: (GameState) -> Unit = {}
) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle(initial = GameState(updatedAt = 0L))

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = object : LifecycleObserver {
            override fun onStateChanged(state: Lifecycle.State) {
                when (state) {
                    Lifecycle.State.Active -> viewModel.onStart()
                    Lifecycle.State.InActive -> viewModel.onStop()
                    else -> {}
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.onStop()
            onDispose(gameState)
        }
    }

    if (gameState.isUpdatedRecently()) {
        VerticalScreen(
            viewModel = viewModel,
            gameState = gameState,
            modifier = modifier
        )
    } else {
        LoadingScreen(
            modifier = modifier
        )
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(Res.string.loading)
        CircularProgressIndicator(
            modifier = Modifier
                .size(128.dp)
        )
    }
}

@Composable
fun VerticalScreen(
    viewModel: MainViewModel,
    gameState: GameState,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        GameInfo(
            deposit = gameState.deposit,
            cloudStorage = gameState.getCloudStorage(),
            isModuleVisible = viewModel::isModuleVisible,
            isModuleEnabled = viewModel::isModuleEnabled
        )
        Divider()
        TaskList(
            state = gameState.tasks,
            visible = viewModel.isTasksViewVisible(),
            isTaskVisible = viewModel::isTaskVisible,
            onTaskClick = viewModel::onTaskClick
        )
        Divider()
        ActionList(
            isActionVisible = viewModel::isActionVisible,
            isActionEnabled = viewModel::isActionEnabled,
            onClick = viewModel::onActionClick
        )
    }
}
