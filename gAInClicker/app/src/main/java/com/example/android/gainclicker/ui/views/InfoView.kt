package com.example.android.gainclicker.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.gainclicker.core.CloudStorage
import com.example.android.gainclicker.core.Currency
import com.example.android.gainclicker.core.Deposit
import com.example.android.gainclicker.core.IOModule
import com.example.android.gainclicker.core.Module
import com.example.android.gainclicker.ui.theme.GAInClickerTheme
import com.example.android.gainclicker.ui.title

@Composable
fun InfoView(
    deposit: Deposit,
    cloudStorage: CloudStorage?,
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

        with(cloudStorage ?: CloudStorage()) {
            CloudStorageView(
                cloudStorage = this,
                visible = isModuleVisible(this),
                enabled = isModuleEnabled(this)
            )
        }

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
            Currency.MEMORY_BIN,
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
        IOModule.values().forEach {
            IoModuleView(
                it,
                isModuleVisible(it),
                isModuleEnabled(it)
            )
        }
    }
}

@Composable
fun ModuleCard(
    visible: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)
) {
    AnimatedVisibility(visible = visible) {
        ElevatedCard(
            shape = RectangleShape,
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = modifier.alpha(if (enabled) 1.0f else 0.4f),
            content = content
        )
    }
}

@Composable
fun IoModuleView(
    module: IOModule,
    visible: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    ModuleCard(
        visible = visible,
        enabled = enabled,
        modifier = modifier
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

@Composable
fun CloudStorageView(
    cloudStorage: CloudStorage,
    visible: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    ModuleCard(
        visible = visible,
        enabled = enabled,
        modifier = modifier
    ) {
        Row(
            modifier = modifier
                .height(IntrinsicSize.Min)
                .padding(8.dp)
        ) {

            Text(
                text = cloudStorage.title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
            ) {

                ProgressBar(
                    progress = cloudStorage.progress,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                ProgressGainView(CloudStorage.gain)
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
            isModuleEnabled = { it is IOModule && it.ordinal % 2 == 0 }
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
            cloudStorage = CloudStorage(0.5f),
            isModuleVisible = { true },
            isModuleEnabled = { it !is IOModule || it.ordinal % 2 == 0 }
        )
    }
}
