package ui

import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.example.android.gainclicker.common.Res
import settings.UiMode
import ui.settings.SettingsScreen
import com.example.android.gainclicker.ui.theme.GAInClickerTheme
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.lifecycle.Lifecycle
import moe.tlaster.precompose.lifecycle.LifecycleObserver
import moe.tlaster.precompose.lifecycle.LocalLifecycleOwner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GAInClickerApp(
    modifier: Modifier = Modifier,
    viewModel: GAInClickerViewModel = koinViewModel(GAInClickerViewModel::class)
) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    val uiMode by viewModel.uiMode.collectAsStateWithLifecycle(initial = UiMode.SYSTEM)

    var showSettings by rememberSaveable {
        mutableStateOf(false)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = object : LifecycleObserver {
            override fun onStateChanged(state: Lifecycle.State) {
                when (state) {
                    Lifecycle.State.Active -> viewModel.onStart()
                    Lifecycle.State.InActive -> viewModel.onStop()
                    else -> {}
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.onStop()
        }
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
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
                    uiMode = uiMode,
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

