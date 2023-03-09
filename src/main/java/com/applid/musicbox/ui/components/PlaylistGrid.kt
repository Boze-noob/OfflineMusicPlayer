package com.applid.musicbox.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.applid.musicbox.services.groove.GrooveKinds
import com.applid.musicbox.services.groove.Playlist
import com.applid.musicbox.services.groove.PlaylistRepository
import com.applid.musicbox.services.groove.PlaylistSortBy
import com.applid.musicbox.ui.helpers.ViewContext

@Composable
fun PlaylistGrid(
    context: ViewContext,
    playlists: List<Playlist>,
    leadingContent: @Composable () -> Unit = {},
) {
    var sortBy by remember {
        mutableStateOf(
            context.symphony.settings.getLastUsedPlaylistsSortBy() ?: PlaylistSortBy.TITLE,
        )
    }
    var sortReverse by remember {
        mutableStateOf(context.symphony.settings.getLastUsedPlaylistsSortReverse())
    }
    val sortedPlaylists by remember {
        derivedStateOf { PlaylistRepository.sort(playlists, sortBy, sortReverse) }
    }

    MediaSortBarScaffold(
        mediaSortBar = {
            Column {
                leadingContent()
                MediaSortBar(
                    context,
                    reverse = sortReverse,
                    onReverseChange = {
                        sortReverse = it
                        context.symphony.settings.setLastUsedPlaylistsSortReverse(it)
                    },
                    sort = sortBy,
                    sorts = PlaylistSortBy.values().associateWith { x -> { x.label(it) } },
                    onSortChange = {
                        sortBy = it
                        context.symphony.settings.setLastUsedPlaylistsSortBy(it)
                    },
                    label = {
                        Text(context.symphony.t.XPlaylists(playlists.size))
                    },
                )
            }
        },
        content = {
            ResponsiveGrid {
                itemsIndexed(
                    sortedPlaylists,
                    key = { i, x -> "$i-${x.id}" },
                    contentType = { _, _ -> GrooveKinds.PLAYLIST }
                ) { _, playlist ->
                    PlaylistTile(context, playlist)
                }
            }
        }
    )
}

private fun PlaylistSortBy.label(context: ViewContext) = when (this) {
    PlaylistSortBy.CUSTOM -> context.symphony.t.custom
    PlaylistSortBy.TITLE -> context.symphony.t.title
    PlaylistSortBy.TRACKS_COUNT -> context.symphony.t.trackCount
}
