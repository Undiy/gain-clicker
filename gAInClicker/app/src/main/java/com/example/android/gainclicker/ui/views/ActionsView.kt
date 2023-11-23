package com.example.android.gainclicker.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.gainclicker.core.ClickAction
import com.example.android.gainclicker.core.Deposit
import com.example.android.gainclicker.core.GameState
import com.example.android.gainclicker.ui.theme.GAInClickerTheme
import com.example.android.gainclicker.ui.title

@Composable
fun ActionsView(
    gameState: GameState,
    onClick: (ClickAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(16.dp)
    ) {
        ClickAction.values().forEach {
            AnimatedVisibility(
                visible = it.isVisible(gameState)
            ) {
                ActionView(
                    action = it,
                    enabled = it.isAcquirable(gameState),
                    onClick = { onClick(it) }
                )
            }
        }
    }
}

@Composable
fun ActionView(
    action: ClickAction,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier
                .size(160.dp, 64.dp)
        ) {
            Text(
                text = action.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }

        action.cost.forEach() {
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "-${it.value}\n${it.currency.title.toLowerCase(Locale.current)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ActionsViewPreview() {
    GAInClickerTheme {
        ActionsView(
            gameState = GameState(
                deposit = Deposit(
                    neurons = 1000,
                    datasets = 1000,
                    processingUnits = 1000
                )
            ),
            onClick = {}
        )
    }
}
