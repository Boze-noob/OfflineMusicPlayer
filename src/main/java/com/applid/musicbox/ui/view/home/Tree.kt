package com.applid.musicbox.ui.view.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.applid.musicbox.ui.components.IconTextBody
import com.applid.musicbox.ui.components.LoaderScaffold
import com.applid.musicbox.ui.components.SongTreeList
import com.applid.musicbox.ui.helpers.ViewContext

@Composable
fun TreeView(context: ViewContext, data: HomeViewData) {
    LoaderScaffold(context, isLoading = data.songsIsUpdating) {
        when {
            data.songs.isNotEmpty() -> SongTreeList(
                context,
                songs = data.songs,
                initialDisabled = context.symphony.settings.getLastDisabledTreePaths(),
                onDisable = { paths ->
                    context.symphony.settings.setLastDisabledTreePaths(paths)
                },
            )
            else -> IconTextBody(
                icon = { modifier ->
                    Icon(
                        Icons.Default.MusicNote,
                        null,
                        modifier = modifier,
                    )
                },
                content = { Text(context.symphony.t.damnThisIsSoEmpty) }
            )
        }
    }
}
