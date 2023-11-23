package com.example.android.gainclicker.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.gainclicker.core.Deposit
import com.example.android.gainclicker.core.GameState
import com.example.android.gainclicker.core.Module
import com.example.android.gainclicker.core.Task
import com.example.android.gainclicker.core.TaskState
import com.example.android.gainclicker.ui.theme.GAInClickerTheme
import com.example.android.gainclicker.ui.title

@Composable
fun TasksView(
    gameState: GameState,
    onTaskClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Tasks (${gameState.tasks.taskThreads.size}/${gameState.tasks.threadSlots} threads)",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        gameState.tasks.tasks.forEach {
            AnimatedVisibility(
                visible = it.task.isVisible(gameState)
            ) {
                TaskView(
                    task = it,
                    isRunning = it.task in gameState.tasks.taskThreads,
                    onClick = { onTaskClick(it.task) }
                )
            }
        }
    }
}

@Composable
fun TaskView(
    task: TaskState,
    isRunning: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
    ) {

        val backgroundColor by animateColorAsState(
            targetValue = if (isRunning) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.secondaryContainer
            }
        )
        val contentColor by animateColorAsState(
            targetValue = if (isRunning) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSecondaryContainer
            }
        )

        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = contentColor
            ),
            modifier = Modifier
                .size(160.dp, 64.dp)
        ) {
            Text(
                text = task.task.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxHeight()
        ) {
            LinearProgressIndicator(
                progress = task.progress,
                color = backgroundColor,
                modifier = Modifier.height(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "TODO",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TasksViewPreview() {
    GAInClickerTheme {
        TasksView(
            gameState = GameState(
                deposit = Deposit(
                    neurons = 1000,
                    datasets = 1000,
                    processingUnits = 1000
                ),
                modules = Module.values().toSet()
            ),
            onTaskClick = {}
        )
    }
}