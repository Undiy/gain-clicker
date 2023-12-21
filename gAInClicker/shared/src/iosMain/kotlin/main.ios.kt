import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.PreComposeApplication
import ui.GAInClickerApp


fun MainViewController() = PreComposeApplication {
    GAInClickerApp(
        modifier = Modifier.fillMaxSize()
    )
}
