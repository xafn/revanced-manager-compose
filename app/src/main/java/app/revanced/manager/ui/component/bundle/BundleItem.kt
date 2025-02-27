package app.revanced.manager.ui.component.bundle

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.revanced.manager.R
import app.revanced.manager.domain.bundles.PatchBundleSource
import app.revanced.manager.domain.bundles.PatchBundleSource.Companion.propsOrNullFlow
import kotlinx.coroutines.flow.map

@Composable
fun BundleItem(
    bundle: PatchBundleSource,
    onDelete: () -> Unit,
    onUpdate: () -> Unit
) {
    var viewBundleDialogPage by rememberSaveable { mutableStateOf(false) }
    val state by bundle.state.collectAsStateWithLifecycle()

    val version by remember(bundle) {
        bundle.propsOrNullFlow().map { props -> props?.versionInfo?.patches }
    }.collectAsStateWithLifecycle(null)

    if (viewBundleDialogPage) {
        BundleInformationDialog(
            onDismissRequest = { viewBundleDialogPage = false },
            onDeleteRequest = {
                viewBundleDialogPage = false
                onDelete()
            },
            bundle = bundle,
            onRefreshButton = onUpdate,
        )
    }

    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                viewBundleDialogPage = true
            },
        headlineContent = {
            Text(
                text = bundle.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        supportingContent = {
            state.patchBundleOrNull()?.patches?.size?.let { patchCount ->
                Text(
                    text = pluralStringResource(R.plurals.patches_count, patchCount, patchCount),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        trailingContent = {
            Row {
                val icon = remember(state) {
                    when (state) {
                        is PatchBundleSource.State.Failed -> Icons.Outlined.ErrorOutline to R.string.bundle_error
                        is PatchBundleSource.State.Missing -> Icons.Outlined.Warning to R.string.bundle_missing
                        is PatchBundleSource.State.Loaded -> null
                    }
                }

                icon?.let { (vector, description) ->
                    Icon(
                        imageVector = vector,
                        contentDescription = stringResource(description),
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                }

                version?.let { txt ->
                    Text(
                        text = txt,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
    )
}