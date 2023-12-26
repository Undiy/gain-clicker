package ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ui.elements.ActionList
import ui.theme.GAInClickerTheme


@Composable
@Preview(showBackground = true)
private fun ActionListPreview() {
    GAInClickerTheme {
        ActionList(
            isActionVisible = { true },
            isActionEnabled = {
                it.ordinal % 2 == 0
            },
            onClick = {}
        )
    }
}
