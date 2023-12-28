import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.PreComposeApp
import org.koin.compose.KoinContext
import ui.GAInClickerApp

@Composable
fun App() {
    PreComposeApp {
        KoinContext {
            GAInClickerApp(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
