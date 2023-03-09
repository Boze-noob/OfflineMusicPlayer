package com.applid.musicbox.ui.components

import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.applid.musicbox.services.groove.Artist
import com.applid.musicbox.services.groove.ArtistRepository
import com.applid.musicbox.services.groove.ArtistSortBy
import com.applid.musicbox.services.groove.GrooveKinds
import com.applid.musicbox.ui.helpers.ViewContext

@Composable
fun ArtistGrid(
    context: ViewContext,
    artists: List<Artist>,
) {
    ArtistGrid(context, artists, isAlbumArtist = false)
}

@Composable
fun AlbumArtistGrid(
    context: ViewContext,
    artists: List<Artist>,
) {
    ArtistGrid(context, artists, isAlbumArtist = true)
}

@Composable
internal fun ArtistGrid(
    context: ViewContext,
    artists: List<Artist>,
    isAlbumArtist: Boolean,
) {
    var sortBy by remember {
        mutableStateOf(
            context.symphony.settings.getLastUsedArtistsSortBy() ?: ArtistSortBy.ARTIST_NAME
        )
    }
    var sortReverse by remember {
        mutableStateOf(context.symphony.settings.getLastUsedArtistsSortReverse())
    }
    val sortedArtists by remember {
        derivedStateOf { ArtistRepository.sort(artists, sortBy, sortReverse) }
    }

    MediaSortBarScaffold(
        mediaSortBar = {
            MediaSortBar(
                context,
                reverse = sortReverse,
                onReverseChange = {
                    sortReverse = it
                    context.symphony.settings.setLastUsedArtistsSortReverse(it)
                },
                sort = sortBy,
                sorts = ArtistSortBy.values().associateWith { x -> { x.label(it) } },
                onSortChange = {
                    sortBy = it
                    context.symphony.settings.setLastUsedArtistsSortBy(it)
                },
                label = {
                    Text(context.symphony.t.XArtists(artists.size))
                },
            )
        },
        content = {
            ResponsiveGrid {
                itemsIndexed(
                    sortedArtists,
                    key = { i, x -> "$i-${x.name}" },
                    contentType = { _, _ -> GrooveKinds.ARTIST }
                ) { _, artist ->
                    ArtistTile(context, artist, isAlbumArtist)
                }
            }
        }
    )
}

private fun ArtistSortBy.label(context: ViewContext) = when (this) {
    ArtistSortBy.CUSTOM -> context.symphony.t.custom
    ArtistSortBy.ARTIST_NAME -> context.symphony.t.artist
    ArtistSortBy.ALBUMS_COUNT -> context.symphony.t.albumCount
    ArtistSortBy.TRACKS_COUNT -> context.symphony.t.trackCount
}
