package com.example.android.gainclicker.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.gainclicker.Res
import com.example.android.gainclicker.core.GameState
import com.example.android.gainclicker.ui.views.ActionsView
import com.example.android.gainclicker.ui.views.InfoView
import com.example.android.gainclicker.ui.views.TasksView


@Composable
fun MainScreen(
    viewModel: GAInClickerViewModel,
    gameState: GameState,
    modifier: Modifier = Modifier
) {
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