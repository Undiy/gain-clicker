import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import core.GameState
import data.GameStateRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.runBlocking
import moe.tlaster.precompose.PreComposeApp
import org.koin.compose.KoinContext
import org.koin.java.KoinJavaComponent.inject
import ui.GAInClickerApp

@Composable
fun App() {
    PreComposeApp {
        KoinContext {
            val gameStateRepository: GameStateRepository by inject(GameStateRepository::class.java)
            var gameState: GameState? by remember { mutableStateOf(null) }

            GAInClickerApp(
                onGameStateChanged = { gameState = it }
            )

            DisposableEffect(Unit) {
                onDispose {
                    gameState?.also { gameStateLatest ->
                        runBlocking {
                            Napier.i("Saving state before closing GameStateRepository...")
                            gameStateRepository.updateGameState { gameStateLatest }
                        }
                    }
                    Napier.i("Trying to close GameStateRepository...")
                    gameStateRepository.close()
                    Napier.i("Closed GameStateRepository")
                }
            }
        }
    }
}

