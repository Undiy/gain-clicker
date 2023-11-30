package com.example.android.gainclicker.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android.gainclicker.core.GameState
import com.example.android.gainclicker.ui.theme.GAInClickerTheme
import com.example.android.gainclicker.ui.views.ActionsView
import com.example.android.gainclicker.ui.views.InfoView
import com.example.android.gainclicker.ui.views.TasksView
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun GAInClickerApp(
    modifier: Modifier = Modifier,
    viewModel: GAInClickerViewModel = viewModel(factory = GAInClickerViewModel.factory)
) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()

    LifecycleStartEffect(Unit) {
        viewModel.startUpdater()

        onStopOrDispose {
            viewModel.stopUpdater()
        }
    }

    if (gameState.isUpdatedRecently()) {
        VerticalScreen(
            viewModel = viewModel,
            gameState = gameState,
            modifier = modifier
        )
    } else {
        LoadingScreen(modifier)
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
        CircularProgressIndicator(
            modifier = Modifier
                .size(128.dp)
        )
    }
}

@Composable
fun VerticalScreen(
    viewModel: GAInClickerViewModel,
    gameState: GameState,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        InfoView(
            deposit = gameState.deposit,
            cloudStorage = gameState.getCloudStorage(),
            isModuleVisible = viewModel::isModuleVisible,
            isModuleEnabled = viewModel::isModuleEnabled
        )
        Divider()
        TasksView(
            state = gameState.tasks,
            visible = viewModel.isTasksViewVisible(),
            isTaskVisible = viewModel::isTaskVisible,
            onTaskClick = viewModel::onTaskClick
        )
        Divider()
        ActionsView(
            isActionVisible = viewModel::isActionVisible,
            isActionEnabled = viewModel::isActionEnabled,
            onClick = viewModel::onActionClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GAInClickerAppPreview() {
    GAInClickerTheme {
        GAInClickerApp(
            modifier = Modifier.fillMaxSize()
        )
    }
}
