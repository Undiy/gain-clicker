package com.example.android.gainclicker.ui.views

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import com.example.android.gainclicker.core.CurrencyAmount
import com.example.android.gainclicker.ui.TASK_UPDATE_INTERVAL
import com.example.android.gainclicker.ui.title

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
                    TASK_UPDATE_INTERVAL
                } else {
                    TASK_UPDATE_INTERVAL / 2
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

@Composable
fun ProgressGainView(
    gain: List<CurrencyAmount>,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        gain.forEachIndexed { index, amount ->
            if (index != 0) {
                Spacer(modifier = Modifier.width(16.dp))
            }
            Text(
                text = "+${amount.value}\n${amount.currency.title.toLowerCase(Locale.current)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}