package com.example.android.gainclicker.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android.gainclicker.Res
import com.example.android.gainclicker.settings.UiMode
import com.example.android.gainclicker.ui.theme.GAInClickerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GAInClickerApp(
    modifier: Modifier = Modifier,
    viewModel: GAInClickerViewModel = viewModel(factory = GAInClickerViewModel.factory)
) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    val uiMode by viewModel.uiMode.collectAsStateWithLifecycle(initialValue = UiMode.SYSTEM)

    var showSettings by rememberSaveable {
        mutableStateOf(false)
    }

    LifecycleStartEffect(Unit) {
        viewModel.onStart()

        onStopOrDispose {
            viewModel.onStop()
        }
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    GAInClickerTheme(
        darkTheme = when(uiMode) {
            UiMode.SYSTEM -> isSystemInDarkTheme()
            UiMode.LIGHT -> false
            UiMode.DARK -> true
        }
    ) {
        Scaffold(
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                GAInClickerTopAppBar(
                    title = if (showSettings) {
                        Res.string.settings_title
                    } else {
                        Res.string.main_title
                    },
                    canNavigateBack = showSettings,
                    showSettingsAction = !showSettings,
                    scrollBehavior = scrollBehavior,
                    navigateUp = { showSettings = false },
                    navigateToSettings = { showSettings = true }
                )
            }
        ) {
            if (showSettings) {
                SettingsScreen(
                    darkTheme = uiMode,
                    onDarkThemeChanged = viewModel::setUiMode,
                    onBack = { showSettings = false },
                    modifier = modifier.padding(it)
                )
            } else {
                MainScreen(
                    viewModel = viewModel,
                    gameState = gameState,
                    modifier = modifier.padding(it)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GAInClickerTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    showSettingsAction: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
    navigateToSettings: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(title)
        },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = Res.string.back_button
                    )
                }
            }
        },
        actions = {
            if (showSettingsAction) {
                IconButton(onClick = navigateToSettings) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = Res.string.settings_title
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
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
