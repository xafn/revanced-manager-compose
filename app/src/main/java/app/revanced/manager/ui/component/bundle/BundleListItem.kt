package app.revanced.manager.ui.component.bundle

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BundleListItem(
    modifier: Modifier = Modifier,
    headlineText: String,
    supportingText: String = "",
    trailingContent: @Composable (() -> Unit)? = null,
) {
    ListItem(
        headlineContent = {
            Text(
                text = headlineText,
            )
        },
        supportingContent = {
            Text(
                text = supportingText
            )
        },
        trailingContent = trailingContent,
        modifier = modifier
    )
}