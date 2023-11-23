package com.example.android.gainclicker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android.gainclicker.ui.views.InfoView
import com.example.android.gainclicker.ui.views.ActionsView

@Composable
fun GAInClickerApp(modifier: Modifier) {
    val viewModel: GAInClickerViewModel = viewModel()
    val gameState by viewModel.gameState.collectAsState()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        InfoView(
            deposit = gameState.deposit,
            modifier = Modifier
                .fillMaxWidth(0.6f)
        )
        ActionsView(
            gameState = gameState,
            onClick = viewModel::onActionClick
        )
    }
}
