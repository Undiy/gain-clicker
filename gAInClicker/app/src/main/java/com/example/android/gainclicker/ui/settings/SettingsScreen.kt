package com.example.android.gainclicker.ui.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android.gainclicker.Res
import com.example.android.gainclicker.settings.UiMode
import com.example.android.gainclicker.ui.title

@Composable
fun SettingsScreen(
    uiMode: UiMode,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.factory)
) {
    BackHandler(
        onBack = onBack
    )
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        UiModeSetting(
            uiMode = uiMode,
            onUiModeSelected = viewModel::setUiMode
        )

        ResetProgressButton(
            onResetProgress = viewModel::resetProgress
        )
    }
}

@Composable
private fun UiModeSetting(
    uiMode: UiMode,
    onUiModeSelected: (UiMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = Res.string.ui_mode_title,
            modifier = Modifier.alignByBaseline()
        )

        Spacer(modifier = Modifier.width(8.dp))

        var showMenu by remember {
            mutableStateOf(false)
        }

        Box(
            modifier = Modifier.alignByBaseline()
        ) {
            TextButton(
                onClick = { showMenu = true }
            ) {
                Text(text = uiMode.title)
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
            ) {

                UiMode.values().forEach {
                    DropdownMenuItem(
                        text = { Text(it.title) },
                        onClick = {
                            onUiModeSelected(it)
                            showMenu = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ResetProgressButton(
    onResetProgress: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }

    ElevatedButton(
        onClick = { showDialog = true },
        modifier = modifier
    ) {
        Text(text = Res.string.reset_progress_button)
    }

    if (showDialog) {
        val dismiss = { showDialog = false }
        AlertDialog(
            onDismissRequest = dismiss,
            title = { Text(Res.string.reset_progress_dialog_title) },
            text = { Text(Res.string.reset_progress_dialog_text) },
            confirmButton = {
                TextButton(onClick = {
                    onResetProgress()
                    dismiss()
                }) {
                    Text(Res.string.reset_progress_dialog_confirm)
                }
            },
            dismissButton = {
                TextButton(onClick = dismiss) {
                    Text(Res.string.reset_progress_dialog_dismiss)
                }
            }
        )
    }
}