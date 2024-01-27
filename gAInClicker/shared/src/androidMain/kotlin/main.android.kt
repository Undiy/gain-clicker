import androidx.compose.runtime.Composable
import moe.tlaster.precompose.PreComposeApp
import org.koin.compose.KoinContext
import ui.GAInClickerApp

@Composable
fun App() {
    PreComposeApp {
        KoinContext {
            GAInClickerApp()
        }
    }
}
