package com.applid.musicbox.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.applid.musicbox.ui.components.*

import com.applid.musicbox.ui.helpers.ViewContext
import com.applid.musicbox.utils.swap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreView(context: ViewContext, genre: String) {
    val songs = remember {
        context.symphony.groove.song.getSongsOfGenre(genre).toMutableStateList()
    }
    var isViable by remember { mutableStateOf(songs.isNotEmpty()) }

    val onGenreUpdate = {
        songs.swap(context.symphony.groove.song.getSongsOfGenre(genre))
        isViable = songs.isNotEmpty()
    }

    EventerEffect(context.symphony.groove.genre.onUpdate) { onGenreUpdate() }
    EventerEffect(context.symphony.groove.song.onUpdate) { onGenreUpdate() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { context.navController.popBackStack() }
                    ) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                title = {
                    TopAppBarMinimalTitle {
                        Text("${context.symphony.t.genre} - $genre")
                    }
                },
                actions = {
                    var showOptionsMenu by remember { mutableStateOf(false) }

                    IconButton(
                        onClick = {
                            showOptionsMenu = !showOptionsMenu
                        }
                    ) {
                        Icon(Icons.Default.MoreVert, null)
                        GenericSongListDropdown(
                            context,
                            songs = songs,
                            expanded = showOptionsMenu,
                            onDismissRequest = {
                                showOptionsMenu = false
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
            )
        },
        content = { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                when {
                    isViable -> SongList(
                        context,
                        songs = songs
                    )
                    else -> UnknownGenre(context, genre)
                }
            }
        },
        bottomBar = {
            NowPlayingBottomBar(context)
        }
    )
}

@Composable
private fun UnknownGenre(context: ViewContext, genre: String) {
    IconTextBody(
        icon = { modifier ->
            Icon(
                Icons.Default.Tune,
                null,
                modifier = modifier
            )
        },
        content = {
            Text(context.symphony.t.unknownGenreX(genre))
        }
    )
}
