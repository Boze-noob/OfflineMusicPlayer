package com.applid.musicbox.ui.components

import androidx.compose.runtime.Composable
import com.applid.musicbox.services.groove.Playlist
import com.applid.musicbox.ui.helpers.ViewContext

@Composable
fun PlaylistInformationDialog(
    context: ViewContext,
    playlist: Playlist,
    onDismissRequest: () -> Unit
) {
    InformationDialog(
        context,
        content = {
            InformationKeyValue(
                context.symphony.t.title,
                playlist.title
            )
            InformationKeyValue(
                context.symphony.t.trackCount,
                playlist.numberOfTracks.toString()
            )
            InformationKeyValue(
                context.symphony.t.isLocalPlaylist,
                when {
                    playlist.isLocal() -> context.symphony.t.yes
                    else -> context.symphony.t.no
                },
            )
            playlist.local?.let { local ->
                InformationKeyValue(
                    context.symphony.t.path,
                    local.path,
                )
            }
        },
        onDismissRequest = onDismissRequest,
    )
}
