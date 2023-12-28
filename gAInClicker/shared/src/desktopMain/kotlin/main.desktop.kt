import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            GAInClickerApp(
                modifier = Modifier.fillMaxSize(),
                onDispose = { gameState ->
                    runBlocking {
                        Napier.i("Saving state before closing GameStateRepository...")
                        gameStateRepository.updateGameState { gameState }
                    }
                    Napier.i("Trying to close GameStateRepository...")
                    gameStateRepository.close()
                    Napier.i("Closed GameStateRepository")
                }
            )
        }
    }
}

