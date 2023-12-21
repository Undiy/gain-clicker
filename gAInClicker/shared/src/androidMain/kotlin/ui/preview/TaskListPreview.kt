package ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.gainclicker.ui.theme.GAInClickerTheme
import core.MAX_TASK_THREAD_SLOTS
import core.Task
import core.TasksState
import ui.elements.TaskList

@Preview(showBackground = true)
@Composable
private fun TaskListPreview() {
    GAInClickerTheme {
        TaskList(
            state = TasksState(
                threadSlots = MAX_TASK_THREAD_SLOTS,
                taskThreads = setOf(Task.DATASET_ACCRUAL)
            ),
            visible = true,
            isTaskVisible = { true },
            onTaskClick = {}
        )
    }
}
