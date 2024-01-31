import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import di.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import undiy.games.gainclicker.common.Res

fun main() {
    initKoin()

    Napier.base(DebugAntilog())

    application {
        val state = rememberWindowState(
	        position = WindowPosition(Alignment.Center)
	    )

        Window(
            onCloseRequest = ::exitApplication,
            state = state,
            title = Res.string.main_title
        ) {
            App()
        }
    }
}
