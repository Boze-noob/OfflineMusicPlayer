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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.applid.musicbox.services.groove.Song
import com.applid.musicbox.services.radio.RadioEvents
import com.applid.musicbox.ui.helpers.RoutesBuilder
import com.applid.musicbox.ui.helpers.ViewContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongCard(
    context: ViewContext,
    song: Song,
    highlighted: Boolean = false,
    autoHighlight: Boolean = true,
    disableHeartIcon: Boolean = false,
    leading: @Composable () -> Unit = {},
    thumbnailLabel: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
) {
    var isCurrentPlaying by remember {
        mutableStateOf(autoHighlight && song.id == context.symphony.radio.queue.currentPlayingSong?.id)
    }
    var isInFavorites by remember {
        mutableStateOf(context.symphony.groove.playlist.isInFavorites(song.id))
    }

    if (autoHighlight) {
        EventerEffect(context.symphony.radio.onUpdate) {
            if (it == RadioEvents.StartPlaying || it == RadioEvents.StopPlaying) {
                isCurrentPlaying = song.id == context.symphony.radio.queue.currentPlayingSong?.id
            }
        }
    }

    EventerEffect(context.symphony.groove.playlist.onFavoritesUpdate) { favorites ->
        isInFavorites = favorites.contains(song.id)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = onClick
    ) {
        Box(modifier = Modifier.padding(12.dp, 12.dp, 4.dp, 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                leading()
                Box {
                    AsyncImage(
                        song.createArtworkImageRequest(context.symphony, true).build(),
                        null,
                        modifier = Modifier
                            .size(45.dp)
                            .clip(RoundedCornerShape(10.dp)),
                    )
                    thumbnailLabel?.let { it ->
                        Box(
                            modifier = Modifier
                                .offset(y = 8.dp)
                                .align(Alignment.BottomCenter)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.surfaceVariant,
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(3.dp, 0.dp)
                            ) {
                                ProvideTextStyle(
                                    MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                ) { it() }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        song.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = when {
                                highlighted || isCurrentPlaying -> MaterialTheme.colorScheme.primary
                                else -> LocalTextStyle.current.color
                            }
                        )
                    )
                    song.artistName?.let { artistName ->
                        Text(
                            artistName,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Spacer(modifier = Modifier.width(15.dp))


                Row {
                    /*
                    if (!disableHeartIcon && isInFavorites) {
                        IconButton(
                            modifier = Modifier.offset(4.dp, 0.dp),
                            onClick = {
                                context.symphony.groove.playlist.removeFromFavorites(song.id)
                            }
                        ) {
                            Icon(
                                Icons.Default.Favorite,
                                null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                     */


                    var showOptionsMenu by remember { mutableStateOf(false) }
                    IconButton(
                        onClick = { showOptionsMenu = !showOptionsMenu }
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            null,
                            modifier = Modifier.size(24.dp),
                        )
                        SongDropdownMenu(
                            context,
                            song,
                            expanded = showOptionsMenu,
                            onDismissRequest = {
                                showOptionsMenu = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SongDropdownMenu(
    context: ViewContext,
    song: Song,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
) {
    var showInfoDialog by remember { mutableStateOf(false) }
    var showAddToPlaylistDialog by remember { mutableStateOf(false) }
    val isInFavorite = context.symphony.groove.playlist.isInFavorites(song.id)

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        /*
        DropdownMenuItem(
            leadingIcon = {
                Icon(Icons.Default.Favorite, null)
            },
            text = {
                Text(
                    if (isInFavorite) context.symphony.t.unfavorite
                    else context.symphony.t.favorite
                )
            },
            onClick = {
                onDismissRequest()
                context.symphony.groove.playlist.run {
                    when {
                        isInFavorite -> removeFromFavorites(song.id)
                        else -> addToFavorites(song.id)
                    }
                }
            }
        )
         */
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
                    song,
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
                context.symphony.radio.queue.add(song)
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
        song.artistName?.let { artistName ->
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
        song.additional.albumArtist?.let { albumArtist ->
            DropdownMenuItem(
                leadingIcon = {
                    Icon(Icons.Default.Person, null)
                },
                text = {
                    Text(context.symphony.t.viewAlbumArtist)
                },
                onClick = {
                    onDismissRequest()
                    context.navController.navigate(
                        RoutesBuilder.buildArtistRoute(albumArtist)
                    )
                }
            )
        }
        DropdownMenuItem(
            leadingIcon = {
                Icon(Icons.Default.Album, null)
            },
            text = {
                Text(context.symphony.t.viewAlbum)
            },
            onClick = {
                onDismissRequest()
                context.navController.navigate(
                    RoutesBuilder.buildAlbumRoute(song.albumId)
                )
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(Icons.Default.Info, null)
            },
            text = {
                Text(context.symphony.t.details)
            },
            onClick = {
                onDismissRequest()
                showInfoDialog = true
            }
        )
    }

    if (showInfoDialog) {
        SongInformationDialog(
            context,
            song = song,
            onDismissRequest = {
                showInfoDialog = false
            }
        )
    }

    if (showAddToPlaylistDialog) {
        AddToPlaylistDialog(
            context,
            songs = listOf(song.id),
            onDismissRequest = {
                showAddToPlaylistDialog = false
            }
        )
    }
}

