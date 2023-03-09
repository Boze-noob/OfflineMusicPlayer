package com.applid.musicbox.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.applid.musicbox.services.groove.*
import com.applid.musicbox.services.radio.Radio
import com.applid.musicbox.services.radio.RadioEvents
import com.applid.musicbox.ui.components.*
import com.applid.musicbox.ui.helpers.ViewContext
import com.applid.musicbox.utils.swap

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongTreeList(
    context: ViewContext,
    songs: List<Song>,
    initialDisabled: List<String>,
    onDisable: ((List<String>) -> Unit),
) {
    val tree by remember {
        derivedStateOf { createLinearTree(songs) }
    }
    val disabled = remember {
        mutableStateListOf<String>().apply {
            swap(initialDisabled)
        }
    }
    var pathsSortBy by remember {
        mutableStateOf(
            context.symphony.settings.getLastUsedTreePathSortBy() ?: PathSortBy.NAME
        )
    }
    var pathsSortReverse by remember {
        mutableStateOf(context.symphony.settings.getLastUsedTreePathSortReverse())
    }
    var songsSortBy by remember {
        mutableStateOf(
            context.symphony.settings.getLastUsedSongsSortBy() ?: SongSortBy.FILENAME
        )
    }
    var songsSortReverse by remember {
        mutableStateOf(context.symphony.settings.getLastUsedSongsSortReverse())
    }
    val sortedTree by remember {
        derivedStateOf {
            val pairs = GrooveExplorer.sort(tree.keys.toList(), pathsSortBy, pathsSortReverse)
                .map { it to SongRepository.sort(tree[it]!!, songsSortBy, songsSortReverse) }
            mapOf(*pairs.toTypedArray())
        }
    }
    val sortedSongs by remember {
        derivedStateOf { sortedTree.values.flatten().toList() }
    }

    MediaSortBarScaffold(
        mediaSortBar = {
            SongTreeListMediaSortBar(
                context,
                songsCount = songs.size,
                pathsSortBy = pathsSortBy,
                pathsSortReverse = pathsSortReverse,
                songsSortBy = songsSortBy,
                songsSortReverse = songsSortReverse,
                setPathsSortBy = {
                    pathsSortBy = it
                    context.symphony.settings.setLastUsedTreePathSortBy(it)
                },
                setPathsSortReverse = {
                    pathsSortReverse = it
                    context.symphony.settings.setLastUsedTreePathSortReverse(it)
                },
                setSongsSortBy = {
                    songsSortBy = it
                    context.symphony.settings.setLastUsedSongsSortBy(it)
                },
                setSongsSortReverse = {
                    songsSortReverse = it
                    context.symphony.settings.setLastUsedSongsSortReverse(it)
                },
            )
        },
        content = {
            val lazyListState = rememberLazyListState()

            LazyColumn(
                state = lazyListState,
                modifier = Modifier.drawScrollBar(lazyListState),
            ) {
                sortedTree.forEach { (dirname, children) ->
                    val show = !disabled.contains(dirname)
                    val sepPadding = if (show) 4.dp else 0.dp

                    stickyHeader {
                        Box(modifier = Modifier.padding(bottom = sepPadding)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
                                    .clickable {
                                        when {
                                            show -> disabled.add(dirname)
                                            else -> disabled.remove(dirname)
                                        }
                                        onDisable(disabled)
                                    }
                                    .padding(start = 12.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                            ) {
                                Icon(
                                    if (show) Icons.Default.ExpandMore
                                    else Icons.Default.ChevronRight,
                                    null,
                                    modifier = Modifier.size(20.dp),
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(dirname, style = MaterialTheme.typography.labelMedium)
                                Spacer(modifier = Modifier.weight(1f))

                                var showOptionsMenu by remember { mutableStateOf(false) }
                                SongTreeListSongCardIconButton(
                                    icon = { modifier ->
                                        Icon(
                                            Icons.Default.MoreVert,
                                            null,
                                            modifier = modifier,
                                        )
                                        GenericSongListDropdown(
                                            context,
                                            songs = children,
                                            expanded = showOptionsMenu,
                                            onDismissRequest = {
                                                showOptionsMenu = false
                                            }
                                        )
                                    },
                                    onClick = {
                                        showOptionsMenu = !showOptionsMenu
                                    }
                                )
                            }
                        }
                    }

                    if (show) {
                        items(children) { song ->
                            var isCurrentPlaying by remember {
                                mutableStateOf(
                                    song.id == context.symphony.radio.queue.currentPlayingSong?.id
                                )
                            }
                            var isInFavorites by remember {
                                mutableStateOf(context.symphony.groove.playlist.isInFavorites(song.id))
                            }

                            EventerEffect(context.symphony.radio.onUpdate) {
                                if (it == RadioEvents.StartPlaying || it == RadioEvents.StopPlaying) {
                                    isCurrentPlaying =
                                        song.id == context.symphony.radio.queue.currentPlayingSong?.id
                                }
                            }

                            EventerEffect(context.symphony.groove.playlist.onFavoritesUpdate) { favorites ->
                                isInFavorites = favorites.contains(song.id)
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(5.dp))
                                    .clickable {
                                        context.symphony.radio.shorty.playQueue(
                                            sortedSongs,
                                            Radio.PlayOptions(index = sortedSongs.indexOf(song))
                                        )
                                    }
                                    .padding(start = 12.dp, end = 8.dp, top = 6.dp, bottom = 6.dp)
                            ) {
                                AsyncImage(
                                    song.createArtworkImageRequest(context.symphony).build(),
                                    null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(RoundedCornerShape(5.dp)),
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        song.title,
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            color = when {
                                                isCurrentPlaying -> MaterialTheme.colorScheme.primary
                                                else -> LocalTextStyle.current.color
                                            }
                                        ),
                                    )
                                    song.artistName?.let {
                                        Text(
                                            it,
                                            style = MaterialTheme.typography.labelSmall,
                                        )
                                    }
                                }
                                Row {
                                    /*
                                    if (isInFavorites) {
                                        SongTreeListSongCardIconButton(
                                            icon = { modifier ->
                                                Icon(
                                                    Icons.Default.Favorite,
                                                    null,
                                                    modifier = modifier,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                )
                                            },
                                            onClick = {
                                                context.symphony.groove.playlist
                                                    .removeFromFavorites(song.id)
                                            }
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))

                                     */

                                    var showOptionsMenu by remember { mutableStateOf(false) }
                                    SongTreeListSongCardIconButton(
                                        icon = { modifier ->
                                            Icon(
                                                Icons.Default.MoreVert,
                                                null,
                                                modifier = modifier,
                                            )
                                            SongDropdownMenu(
                                                context,
                                                song,
                                                expanded = showOptionsMenu,
                                                onDismissRequest = {
                                                    showOptionsMenu = false
                                                }
                                            )
                                        },
                                        onClick = {
                                            showOptionsMenu = !showOptionsMenu
                                        }
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Divider(modifier = Modifier.padding(top = sepPadding))
                    }
                }
            }
        }
    )
}

@Composable
fun SongTreeListSongCardIconButton(
    icon: @Composable (Modifier) -> Unit,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .clickable { onClick() }
    ) {
        icon(
            Modifier
                .size(20.dp)
                .padding(start = 5.dp, top = 5.dp),
        )
    }
}

@Composable
private fun SongTreeListMediaSortBar(
    context: ViewContext,
    songsCount: Int,
    pathsSortBy: PathSortBy,
    pathsSortReverse: Boolean,
    songsSortBy: SongSortBy,
    songsSortReverse: Boolean,
    setPathsSortBy: (PathSortBy) -> Unit,
    setPathsSortReverse: (Boolean) -> Unit,
    setSongsSortBy: (SongSortBy) -> Unit,
    setSongsSortReverse: (Boolean) -> Unit,
) {
    val currentTextStyle = MaterialTheme.typography.bodySmall.run {
        copy(color = MaterialTheme.colorScheme.onSurface)
    }
    var showSortMenu by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp, 4.dp)
                .clip(RoundedCornerShape(100))
                .clickable {
                    showSortMenu = !showSortMenu
                }
                .padding(8.dp, 8.dp)
        ) {
            ProvideTextStyle(value = currentTextStyle) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 2.dp),
                ) {
                    Icon(
                        if (pathsSortReverse) Icons.Default.ArrowDownward
                        else Icons.Default.ArrowUpward,
                        null,
                        modifier = Modifier.size(12.dp),
                    )
                    Text(pathsSortBy.label(context))
                    Divider(
                        modifier = Modifier
                            .size(9.dp, 12.dp)
                            .padding(4.dp, 0.dp)
                    )
                    Icon(
                        if (songsSortReverse) Icons.Default.ArrowDownward
                        else Icons.Default.ArrowUpward,
                        null,
                        modifier = Modifier.size(12.dp),
                    )
                    Text(songsSortBy.label(context))
                }
            }
            DropdownMenu(
                expanded = showSortMenu,
                onDismissRequest = {
                    showSortMenu = false
                },
            ) {
                Row {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            context.symphony.t.folders,
                            style = currentTextStyle,
                            modifier = Modifier.padding(16.dp, 8.dp),
                        )
                        PathSortBy.values().forEach { sortBy ->
                            SongTreeListMediaSortBarDropdownMenuItem(
                                selected = pathsSortBy == sortBy,
                                reversed = pathsSortReverse,
                                text = { Text(sortBy.label(context)) },
                                onClick = {
                                    when (pathsSortBy) {
                                        sortBy -> setPathsSortReverse(!pathsSortReverse)
                                        else -> setPathsSortBy(sortBy)
                                    }
                                },
                            )
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            context.symphony.t.songs,
                            style = currentTextStyle,
                            modifier = Modifier.padding(16.dp, 8.dp),
                        )
                        SongSortBy.values().forEach { sortBy ->
                            SongTreeListMediaSortBarDropdownMenuItem(
                                selected = songsSortBy == sortBy,
                                reversed = songsSortReverse,
                                text = { Text(sortBy.label(context)) },
                                onClick = {
                                    when (songsSortBy) {
                                        sortBy -> setSongsSortReverse(!songsSortReverse)
                                        else -> setSongsSortBy(sortBy)
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
        Text(
            context.symphony.t.XSongs(songsCount),
            style = currentTextStyle,
            modifier = Modifier.padding(16.dp, 0.dp),
        )
    }
}

@Composable
private fun SongTreeListMediaSortBarDropdownMenuItem(
    selected: Boolean,
    reversed: Boolean,
    text: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        contentPadding = MenuDefaults.DropdownMenuItemContentPadding.run {
            val horizontalPadding = calculateLeftPadding(LayoutDirection.Ltr)
            PaddingValues(
                start = horizontalPadding.div(2),
                end = horizontalPadding.times(4),
            )
        },
        leadingIcon = {
            when {
                selected -> IconButton(
                    content = {
                        Icon(
                            if (reversed) Icons.Default.ArrowCircleDown
                            else Icons.Default.ArrowCircleUp,
                            null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    onClick = onClick,
                )
                else -> RadioButton(
                    selected = false,
                    onClick = onClick,
                )
            }
        },
        text = text,
        onClick = onClick,
    )
}

private fun PathSortBy.label(context: ViewContext) = when (this) {
    PathSortBy.CUSTOM -> context.symphony.t.custom
    PathSortBy.NAME -> context.symphony.t.name
}

private fun createLinearTree(songs: List<Song>): Map<String, List<Song>> {
    val result = mutableMapOf<String, MutableList<Song>>()
    songs.forEach { song ->
        val parsedPath = GrooveExplorer.Path(song.path)
        val dirname = parsedPath.dirname.toString()
        if (!result.containsKey(dirname)) {
            result[dirname] = mutableListOf()
        }
        result[dirname]!!.add(song)
    }
    return result
}
