package com.applid.musicbox.ui.view.settings

import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.applid.musicbox.ui.helpers.ViewContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsLinkTile(
    context: ViewContext,
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    url: String
) {
    Card(
        colors = SettingsTileDefaults.cardColors(),
        onClick = {
            context.symphony.shorty.startBrowserActivity(context.activity, url)
        }
    ) {
        ListItem(
            colors = SettingsTileDefaults.listItemColors(),
            leadingContent = { icon() },
            headlineText = { title() },
            supportingText = { Text(url) }
        )
    }
}
