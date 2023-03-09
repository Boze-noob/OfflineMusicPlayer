package com.applid.musicbox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.applid.musicbox.services.groove.Album
import com.applid.musicbox.ui.helpers.RoutesBuilder
import com.applid.musicbox.ui.helpers.ViewContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumTile(context: ViewContext, album: Album) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = {
            context.navController.navigate(RoutesBuilder.buildAlbumRoute(album.id))
        }
    ) {
        Box(modifier = Modifier.padding(12.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box {
                    AsyncImage(
                        album.createArtworkImageRequest(context.symphony).build(),
                        null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp)),
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 4.dp)
                    ) {
                        var showOptionsMenu by remember { mutableStateOf(false) }
                        IconButton(
                            onClick = { showOptionsMenu = !showOptionsMenu }
                        ) {
                            Icon(Icons.Default.MoreVert, null)
                            AlbumDropdownMenu(
                                context,
                                album,
                                expanded = showOptionsMenu,
                                onDismissRequest = {
                                    showOptionsMenu = false
                                }
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    ) {
                        IconButton(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    RoundedCornerShape(12.dp)
                                )
                                .then(Modifier.size(36.dp)),
                            onClick = {
                                val songs =
                                    context.symphony.groove.song.getSongsOfAlbum(album.id)
                                context.symphony.radio.shorty.playQueue(songs)
                            }
                        ) {
                            Icon(Icons.Default.PlayArrow, null)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    album.name,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
                album.artist?.let { artistName ->
                    Text(
                        artistName,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
fun AlbumDropdownMenu(
    context: ViewContext,
    album: Album,
    expanded: Boolean,
    onDismissRequest: () -> Unit
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
                context.symphony.radio.shorty.playQueue(
                    context.symphony.groove.song.getSongsOfAlbum(album.id),
                    shuffle = true,
                )
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
                    context.symphony.groove.song.getSongsOfAlbum(album.id),
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
                context.symphony.radio.queue.add(
                    context.symphony.groove.song.getSongsOfAlbum(album.id)
                )
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
        album.artist?.let { artistName ->
            DropdownMenuItem(
                leadingIcon = {
                    Icon(Icons.Default.Person, null)
                },
                text = {
                    Text(context.symphony.t.viewArtist)
                },
                onClick = {
                    onDismissRequest()
                    context.navController.navigate(
                        RoutesBuilder.buildArtistRoute(artistName)
                    )
                }
            )
        }
    }

    if (showAddToPlaylistDialog) {
        AddToPlaylistDialog(
            context,
            songs = context.symphony.groove.song.getSongsOfAlbum(album.id).map { it.id },
            onDismissRequest = {
                showAddToPlaylistDialog = false
            }
        )
    }
}
