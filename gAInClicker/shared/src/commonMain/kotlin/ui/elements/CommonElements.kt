package ui.elements

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import core.CurrencyAmount
import core.PROGRESS_UPDATE_INTERVAL
import ui.currencyTitle

@Composable
fun ProgressBar(
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    var currentPreviousProgress by remember {
        mutableStateOf(Pair(0.0f, 0.0f))
    }


    LaunchedEffect(key1 = progress) {
        fun isFullProgress(p: Float) = 1.0f - p < 0.01f

        val previousProgress: Float = currentPreviousProgress.first
        val currentProgress = when {
            isFullProgress(progress) -> 1.0f // snap full
            isFullProgress(previousProgress) -> 0.0f // snap empty
            else -> progress
        }
        currentPreviousProgress = Pair(currentProgress, previousProgress)
    }

    currentPreviousProgress.let { (currentProgress, previousProgress) ->
        val animatedProgress by animateFloatAsState(
            targetValue = currentProgress,
            animationSpec = tween(
                durationMillis = if (previousProgress < currentProgress) {
                    PROGRESS_UPDATE_INTERVAL
                } else {
                    PROGRESS_UPDATE_INTERVAL / 2
                },
                easing = if (previousProgress < currentProgress) {
                    LinearEasing
                } else {
                    FastOutSlowInEasing
                }
            ),
            label = "task progress"
        )

        LinearProgressIndicator(
            progress = animatedProgress,
            color = color,
            modifier = modifier
                .height(12.dp)
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProgressGain(
    gain: List<CurrencyAmount>,
    modifier: Modifier = Modifier
) {
    FlowRow(modifier) {
        gain.forEachIndexed { index, amount ->
            if (index != 0) {
                Spacer(modifier = Modifier.width(16.dp))
            }
            Text(
                text = "+${amount.value}\n${amount.currencyTitle}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private const val AUTOSIZE_TEXT_MULTIPLIER = 0.9f
private const val AUTOSIZE_TEXT_MULTIPLIER_MIN = 0.1f

@Composable
fun AutosizeText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    style: TextStyle = LocalTextStyle.current
) {

    var multiplier by remember { mutableStateOf(1f) }

    Text(
        text = text,
        modifier = modifier,
        textAlign = textAlign,
        overflow = TextOverflow.Ellipsis,
        style = style.copy(
            fontSize = style.fontSize * multiplier
        ),
        onTextLayout = {
            if (it.isLineEllipsized(it.lineCount - 1)
                && multiplier > AUTOSIZE_TEXT_MULTIPLIER_MIN
            ) {
                multiplier *= AUTOSIZE_TEXT_MULTIPLIER
            }
        }
    )
}
