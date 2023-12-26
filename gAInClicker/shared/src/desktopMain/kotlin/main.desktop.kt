import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import data.GameStateRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.runBlocking
import moe.tlaster.precompose.koin.koinViewModel
import org.koin.java.KoinJavaComponent.inject
import ui.GAInClickerApp
import ui.GAInClickerViewModel

@Composable
fun App() {
    val gameStateRepository: GameStateRepository by inject(GameStateRepository::class.java)
    val viewModel: GAInClickerViewModel = koinViewModel(GAInClickerViewModel::class)

    GAInClickerApp(
        modifier = Modifier.fillMaxSize(),
        viewModel = viewModel
    )

    DisposableEffect(Unit) {
        onDispose {
            runBlocking {
                Napier.i("Saving state to close GameStateRepository...")
                gameStateRepository.updateGameState { viewModel.gameState.value }
            }
            Napier.i("Trying to close GameStateRepository...")
            gameStateRepository.close()
            Napier.i("Closed GameStateRepository")
        }
    }
}

