package com.example.android.gainclicker.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.gainclicker.Res
import com.example.android.gainclicker.settings.UiMode

@Composable
fun SettingsScreen(
    darkTheme: UiMode,
    onDarkThemeChanged: (UiMode) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(
        onBack = onBack
    )
    Column(
        modifier = modifier.padding(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = Res.string.ui_mode_title,
                modifier = Modifier.alignByBaseline()
            )
            
            Spacer(modifier = Modifier.width(8.dp))

            var menuExpanded by remember {
                mutableStateOf(false)
            }

            Box(
                modifier = Modifier.alignByBaseline()
            ) {
                TextButton(
                    onClick = { menuExpanded = true }
                ) {
                    Text(text = darkTheme.title)
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                ) {

                    UiMode.values().forEach {
                        DropdownMenuItem(
                            text = { Text(it.title) },
                            onClick = {
                                onDarkThemeChanged(it)
                                menuExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}