package com.applid.musicbox.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.applid.musicbox.ui.helpers.ViewContext
import com.applid.musicbox.utils.mutate
import kotlinx.coroutines.launch

@Composable
fun AddToPlaylistDialog(
    context: ViewContext,
    songs: List<Long>,
    onDismissRequest: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val playlists = remember {
        context.symphony.groove.playlist.getAll().filter {
            it.isNotLocal()
        }
    }

    ScaffoldDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(context.symphony.t.addToPlaylist)
        },
        content = {
            when {
                playlists.isEmpty() -> SubtleCaptionText(context.symphony.t.noInAppPlaylistsFound)
                else -> LazyColumn(modifier = Modifier.padding(bottom = 4.dp)) {
                    items(playlists) { playlist ->
                        GenericGrooveCard(
                            image = playlist.createArtworkImageRequest(context.symphony)
                                .build(),
                            title = {
                                Text(playlist.title)
                            },
                            options = { expanded, onDismissRequest ->
                                PlaylistDropdownMenu(
                                    context,
                                    playlist,
                                    expanded = expanded,
                                    onDismissRequest = onDismissRequest,
                                )
                            },
                            onClick = {
                                coroutineScope.launch {
                                    context.symphony.groove.playlist.updatePlaylistSongs(
                                        playlist = playlist,
                                        songs = playlist.songs.mutate { addAll(songs) },
                                    )
                                    onDismissRequest()
                                }
                            }
                        )
                    }
                }
            }
        },
    )
}
