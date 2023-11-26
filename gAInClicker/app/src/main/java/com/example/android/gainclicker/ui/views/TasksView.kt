package com.example.android.gainclicker.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.gainclicker.core.MAX_TASK_THREAD_SLOTS
import com.example.android.gainclicker.core.Task
import com.example.android.gainclicker.core.TaskState
import com.example.android.gainclicker.core.TaskThreadsState
import com.example.android.gainclicker.ui.TASK_UPDATE_INTERVAL
import com.example.android.gainclicker.ui.theme.GAInClickerTheme
import com.example.android.gainclicker.ui.title

@Composable
fun TasksView(
    state: TaskThreadsState,
    isTaskVisible: (Task) -> Boolean,
    onTaskClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(visible = state.threadSlots > 0) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Tasks (${state.taskThreads.size}/${state.threadSlots} threads)",
                style = MaterialTheme.typography.titleMedium
            )
            state.tasks.forEach {
                AnimatedVisibility(
                    visible = isTaskVisible(it.task)
                ) {
                    TaskView(
                        task = it,
                        isRunning = it.task in state.taskThreads,
                        onClick = { onTaskClick(it.task) }
                    )
                }
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
            },
            label = "task button backgroundColor"
        )
        val contentColor by animateColorAsState(
            targetValue = if (isRunning) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSecondaryContainer
            },
            label = "task button contentColor"
        )

        var previousProgress by remember {
            mutableStateOf(0.0f)
        }
        var currentProgress by remember {
            mutableStateOf(0.0f)
        }


        LaunchedEffect(key1 = task.progress) {
            previousProgress = currentProgress
            currentProgress = when {
                previousProgress == 1.0f
                        || (1.0f - task.progress) < 0.01f -> 0.0f // snap empty
                task.progress < previousProgress -> 1.0f // snap full
                else -> task.progress
            }
        }
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
                style = MaterialTheme.typography.titleSmall
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxHeight()
        ) {

            val progress by animateFloatAsState(
                targetValue = currentProgress,
                animationSpec = tween(
                    durationMillis = if (previousProgress < currentProgress) {
                        TASK_UPDATE_INTERVAL
                    } else {
                        TASK_UPDATE_INTERVAL / 2
                    },
                    easing = if (previousProgress < currentProgress) {
                        LinearEasing
                    } else {
                        FastOutSlowInEasing
                    }
                ),
                label = "task progress"
            )

            LinearProgressIndicator(
                progress = progress,
                color = backgroundColor,
                modifier = Modifier
                    .height(12.dp)
                    .fillMaxWidth()
            )

            Row {
                task.task.gain.forEachIndexed { index, amount ->
                    if (index != 0) {
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    Text(
                        text = "+${amount.value}\n${amount.currency.title.toLowerCase(Locale.current)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TasksViewPreview() {
    GAInClickerTheme {
        TasksView(
            state = TaskThreadsState(
                threadSlots = MAX_TASK_THREAD_SLOTS,
                taskThreads = setOf(Task.DATASET_ACCRUAL)
            ),
            isTaskVisible = { true },
            onTaskClick = {}
        )
    }
}
