package com.applid.musicbox.ui.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.applid.musicbox.services.groove.GrooveKinds
import com.applid.musicbox.ui.components.EventerEffect
import com.applid.musicbox.ui.components.SongCard
import com.applid.musicbox.ui.components.TopAppBarMinimalTitle
import com.applid.musicbox.ui.helpers.ViewContext
import com.applid.musicbox.utils.swap
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueueView(context: ViewContext) {
    val coroutineScope = rememberCoroutineScope()
    val queue = remember {
        context.symphony.radio.queue.currentQueue.toMutableStateList()
    }
    var currentSongIndex by remember {
        mutableStateOf(context.symphony.radio.queue.currentSongIndex)
    }
    val selectedSongIndices = remember { mutableStateListOf<Int>() }
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = currentSongIndex,
    )

    BackHandler {
        context.navController.popBackStack()
    }

    EventerEffect(context.symphony.radio.onUpdate) {
        queue.swap(context.symphony.radio.queue.currentQueue)
        currentSongIndex = context.symphony.radio.queue.currentSongIndex
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TopAppBarMinimalTitle {
                        Text(context.symphony.t.queue)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            context.navController.popBackStack()
                        }
                    ) {
                        Icon(
                            Icons.Default.ExpandMore,
                            null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                actions = {
                    if (selectedSongIndices.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                context.symphony.radio.queue.remove(selectedSongIndices)
                                selectedSongIndices.clear()
                            }
                        ) {
                            Icon(Icons.Default.Delete, null)
                        }
                    }
                    IconButton(
                        onClick = {
                            context.symphony.radio.stop()
                        }
                    ) {
                        Icon(Icons.Default.ClearAll, null)
                    }
                }
            )
        },
        content = { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                if (queue.isEmpty()) {
                    NothingPlayingBody(context)
                } else {
                    LazyColumn(state = listState) {
                        itemsIndexed(
                            queue.toList(),
                            key = { i, id -> "$i-$id" },
                            contentType = { _, _ -> GrooveKinds.SONG },
                        ) { i, _ ->
                            val song = context.symphony.radio.queue.getSongAt(i)!!
                            Box {
                                SongCard(
                                    context,
                                    song,
                                    autoHighlight = false,
                                    highlighted = i == currentSongIndex,
                                    leading = {
                                        Checkbox(
                                            checked = selectedSongIndices.contains(i),
                                            onCheckedChange = {
                                                if (selectedSongIndices.contains(i)) {
                                                    selectedSongIndices.remove(i)
                                                } else {
                                                    selectedSongIndices.add(i)
                                                }
                                            },
                                            modifier = Modifier.offset((-4).dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    },
                                    thumbnailLabel = {
                                        Text((i + 1).toString())
                                    },
                                    onClick = {
                                        context.symphony.radio.jumpTo(i)
                                        coroutineScope.launch {
                                            listState.animateScrollToItem(i)
                                        }
                                    },
                                )
                                if (i < currentSongIndex) {
                                    Box(
                                        modifier = Modifier
                                            .matchParentSize()
                                            .background(
                                                MaterialTheme.colorScheme.background.copy(alpha = 0.3f)
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
