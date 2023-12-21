package ui.elements

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
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import core.ClickAction
import ui.currencyTitle
import ui.title

@Composable
fun ActionList(
    isActionVisible: (ClickAction) -> Boolean,
    isActionEnabled: (ClickAction) -> Boolean,
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
                visible = isActionVisible(it)
            ) {
                ActionItem(
                    action = it,
                    enabled = isActionEnabled(it),
                    onClick = { onClick(it) }
                )
            }
        }
    }
}

@Composable
fun ActionItem(
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
            AutosizeText(
                text = action.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall.copy(
                    lineBreak = LineBreak.Heading
                )
            )
        }

        action.cost.forEach {
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "-${it.value}\n${it.currencyTitle}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

