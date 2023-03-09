package com.applid.musicbox.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.applid.musicbox.services.groove.Song
import com.applid.musicbox.ui.helpers.ViewContext

@Composable
fun GenericSongListDropdown(
    context: ViewContext,
    songs: List<Song>,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
) {
    var showAddToPlaylistDialog by remember { mutableStateOf(false) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            leadingIcon = {
                Icon(Icons.Default.PlaylistPlay, null)
            },
            text = {
                Text(context.symphony.t.shufflePlay)
            },
            onClick = {
                onDismissRequest()
                context.symphony.radio.shorty.playQueue(songs, shuffle = true)
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(Icons.Default.PlaylistPlay, null)
            },
            text = {
                Text(context.symphony.t.playNext)
            },
            onClick = {
                onDismissRequest()
                context.symphony.radio.queue.add(
                    songs,
                    context.symphony.radio.queue.currentSongIndex + 1
                )
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(Icons.Default.PlaylistPlay, null)
            },
            text = {
                Text(context.symphony.t.addToQueue)
            },
            onClick = {
                onDismissRequest()
                context.symphony.radio.queue.add(songs)
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(Icons.Default.PlaylistAdd, null)
            },
            text = {
                Text(context.symphony.t.addToPlaylist)
            },
            onClick = {
                onDismissRequest()
                showAddToPlaylistDialog = true
            }
        )
    }

    if (showAddToPlaylistDialog) {
        AddToPlaylistDialog(
            context,
            songs = songs.map { it.id },
            onDismissRequest = {
                showAddToPlaylistDialog = false
            }
        )
    }
}
