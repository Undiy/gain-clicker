package ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import core.GameState
import kotlinx.coroutines.flow.mapNotNull
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.koin.compose.rememberKoinInject
import settings.SettingsRepository
import settings.UiMode
import ui.main.MainScreen
import ui.settings.SettingsScreen
import ui.theme.GAInClickerTheme
import undiy.games.gainclicker.common.Res


private enum class NavRoute(
    val route: String,
    val title: String
) {
    Main("main", Res.string.main_title),
    Settings("settings", Res.string.settings_title);
}

private fun String.toNavRoute() = NavRoute.entries.find { it.route == this }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GAInClickerApp(
    onDispose: (GameState) -> Unit = {}
) {
    val settingsRepository: SettingsRepository = rememberKoinInject()
    val uiMode by settingsRepository.uiMode.collectAsStateWithLifecycle(initial = UiMode.SYSTEM)

    val navigator = rememberNavigator()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    GAInClickerTheme(
        darkTheme = when(uiMode) {
            UiMode.SYSTEM -> isSystemInDarkTheme()
            UiMode.LIGHT -> false
            UiMode.DARK -> true
        }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                GAInClickerTopAppBar(
                    navigator = navigator,
                    scrollBehavior = scrollBehavior
                )
            }
        ) { padding ->
            NavHost(
                navigator = navigator,
                initialRoute = NavRoute.Main.route,
                navTransition = NavTransition(
                    createTransition = fadeIn(),
                    destroyTransition = fadeOut(),
                    pauseTransition = fadeOut(),
                    resumeTransition = fadeIn(),
                ),
                persistNavState = true,
                modifier = Modifier.padding(padding),
            ) {
                scene(route = NavRoute.Main.route) {
                    MainScreen(
                        modifier = Modifier.fillMaxSize(),
                        onDispose = onDispose,
                    )
                }
                scene(route = NavRoute.Settings.route) {
                    SettingsScreen(
                        uiMode = uiMode,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GAInClickerTopAppBar(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val currentRoute by navigator.currentEntry
        .mapNotNull { it?.route?.route?.toNavRoute() }
        .collectAsStateWithLifecycle(initial = NavRoute.Main)

    val canGoBack by navigator.canGoBack.collectAsStateWithLifecycle(initial = false)

    CenterAlignedTopAppBar(
        title = {
            Text(currentRoute.title)
        },
        modifier = modifier,
        navigationIcon = {
            if (canGoBack) {
                IconButton(onClick = navigator::goBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = Res.string.back_button
                    )
                }
            }
        },
        actions = {
            if (currentRoute == NavRoute.Main) {
                IconButton(onClick = { navigator.navigate(NavRoute.Settings.route) }) {
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

