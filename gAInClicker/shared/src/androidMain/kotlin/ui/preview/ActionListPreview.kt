package ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.gainclicker.ui.theme.GAInClickerTheme
import ui.elements.ActionList


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
