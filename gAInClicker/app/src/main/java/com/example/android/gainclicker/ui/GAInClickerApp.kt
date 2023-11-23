package com.example.android.gainclicker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android.gainclicker.core.GameState
import com.example.android.gainclicker.ui.theme.GAInClickerTheme
import com.example.android.gainclicker.ui.views.InfoView
import com.example.android.gainclicker.ui.views.ActionsView
import com.example.android.gainclicker.ui.views.TasksView

@Composable
fun GAInClickerApp(modifier: Modifier = Modifier) {
    val viewModel: GAInClickerViewModel = viewModel()
    val gameState by viewModel.gameState.collectAsState()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        InfoView(
            deposit = gameState.deposit,
            modifier = Modifier
                .fillMaxWidth(0.6f)
        )
        Divider()
        TasksView(
            gameState = gameState,
            onTaskClick = viewModel::onTaskClick
        )
        Divider()
        ActionsView(
            gameState = gameState,
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
