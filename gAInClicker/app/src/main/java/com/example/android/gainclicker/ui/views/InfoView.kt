package com.example.android.gainclicker.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.gainclicker.core.Currency
import com.example.android.gainclicker.core.Deposit
import com.example.android.gainclicker.core.Module
import com.example.android.gainclicker.ui.theme.GAInClickerTheme
import com.example.android.gainclicker.ui.title

@Composable
fun InfoView(
    deposit: Deposit,
    isModuleVisible: (Module) -> Boolean,
    isModuleEnabled: (Module) -> Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Row {
            NumbersView(
                deposit = deposit,
                modifier = Modifier.weight(1.0f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            IoModulesView(
                isModuleVisible = isModuleVisible,
                isModuleEnabled = isModuleEnabled,
                modifier = Modifier.weight(1.0f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        ModuleCard(
            module = Module.CLOUD_STORAGE,
            visible = isModuleVisible(Module.CLOUD_STORAGE),
            enabled = isModuleEnabled(Module.CLOUD_STORAGE)
        )
    }
}

@Composable
fun NumbersView(
    deposit: Deposit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        listOf(
            Currency.NEURON,
            Currency.DATASET,
            Currency.PROCESSING_UNIT,
            Currency.USER
        ).forEach {
            NumbersRowView(
                title = it.title,
                value = deposit[it].toString()
                        + if (it == Currency.DATASET) "/${deposit[Currency.MEMORY_BIN]}" else ""
            )
        }
    }
}

@Composable
fun NumbersRowView(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Text(
            text = "$title:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.alignByBaseline()
        )
        Spacer(modifier = Modifier.weight(1.0f))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.alignByBaseline()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NumbersViewPreview() {
    GAInClickerTheme {
        NumbersView(
            deposit = Deposit(
                neurons = 1234,
                datasets = 27,
                memoryBins = 49,
                processingUnits = 13
            )
        )
    }
}



@Composable
fun IoModulesView(
    isModuleVisible: (Module) -> Boolean,
    isModuleEnabled: (Module) -> Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Module.values().filter(Module::isIo).forEach {
            ModuleCard(
                it,
                isModuleVisible(it),
                isModuleEnabled(it)
            )
        }
    }
}

@Composable
fun ModuleCard(
    module: Module,
    visible: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(visible = visible) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,

            ),
            modifier = modifier.alpha(if (enabled) 1.0f else 0.4f)
        ) {
            Box(
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = module.title,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IoModulesViewPreview() {
    GAInClickerTheme {
        IoModulesView(
            isModuleVisible = { true },
            isModuleEnabled = { it.ordinal % 2 == 0 }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InfoViewPreview() {
    GAInClickerTheme {
        InfoView(
            deposit = Deposit(
                neurons = 1234,
                datasets = 27,
                memoryBins = 49,
                processingUnits = 13
            ),
            isModuleVisible = { true },
            isModuleEnabled = { it.ordinal % 2 == 0 }
        )
    }
}