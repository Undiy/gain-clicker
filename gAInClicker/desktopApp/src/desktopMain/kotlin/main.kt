import androidx.compose.ui.Alignment
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.example.android.gainclicker.common.Res
import di.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import moe.tlaster.precompose.PreComposeWindow

fun main() = application {
    initKoin()

    Napier.base(DebugAntilog())

    val state = rememberWindowState(
        position = WindowPosition(Alignment.Center)
    )

    PreComposeWindow(
        onCloseRequest = ::exitApplication,
        state = state,
        title = Res.string.main_title
    ) {
        App()
    }
}