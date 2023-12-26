package ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import core.CloudStorage
import core.Deposit
import core.IOModule
import ui.elements.GameInfo
import ui.elements.IoModulesView
import ui.elements.NumbersList
import ui.theme.GAInClickerTheme

@Preview(showBackground = true)
@Composable
private fun NumbersListPreview() {
    GAInClickerTheme {
        NumbersList(
            deposit = Deposit(
                neurons = 1234,
                datasets = 27,
                memoryBins = 49,
                processingUnits = 13
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IoModulesViewPreview() {
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
        GameInfo(
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
