package com.example.android.gainclicker.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.gainclicker.core.Currency
import com.example.android.gainclicker.core.Deposit
import com.example.android.gainclicker.ui.theme.GAInClickerTheme
import com.example.android.gainclicker.ui.title

@Composable
fun InfoView(
    deposit: Deposit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        listOf(
            Currency.NEURON,
            Currency.DATASET,
            Currency.PROCESSING_UNIT,
            Currency.USER
        ).forEach {
            InfoRowView(
                title = it.title,
                value = deposit[it].toString()
                        + if (it == Currency.DATASET) "/${deposit[Currency.MEMORY_BIN]}" else ""
            )
        }
    }
}

@Composable
fun InfoRowView(
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
fun InfoViewPreview() {
    GAInClickerTheme {
        InfoView(deposit = Deposit())
    }
}