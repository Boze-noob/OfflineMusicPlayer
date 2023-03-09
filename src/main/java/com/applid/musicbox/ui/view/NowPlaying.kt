package com.applid.musicbox.ui.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import coil.compose.AsyncImage
import com.applid.musicbox.services.SettingsKeys
import com.applid.musicbox.services.groove.Song
import com.applid.musicbox.services.radio.PlaybackPosition
import com.applid.musicbox.services.radio.RadioLoopMode
import com.applid.musicbox.services.radio.RadioSleepTimer
import com.applid.musicbox.ui.components.EventerEffect
import com.applid.musicbox.ui.components.ScaffoldDialog
import com.applid.musicbox.ui.components.SongDropdownMenu
import com.applid.musicbox.ui.components.TopAppBarMinimalTitle
import com.applid.musicbox.ui.helpers.Routes
import com.applid.musicbox.ui.helpers.ScreenOrientation
import com.applid.musicbox.ui.helpers.ViewContext
import com.applid.musicbox.ui.helpers.navigate

import com.applid.musicbox.utils.DurationFormatter
import java.time.Duration
import java.util.*

private data class PlayerStateData(
    val song: Song,
    val isPlaying: Boolean,
    val currentSongIndex: Int,
    val queueSize: Int,
    val currentLoopMode: RadioLoopMode,
    val currentShuffleMode: Boolean,
    val hasSleepTimer: Boolean,
    val showSongAdditionalInfo: Boolean,
    val enableSeekControls: Boolean,
    val seekBackDuration: Int,
    val seekForwardDuration: Int,
)

@Composable
fun NowPlayingView(context: ViewContext) {
    var song by remember { mutableStateOf(context.symphony.radio.queue.currentPlayingSong) }
    var isPlaying by remember { mutableStateOf(context.symphony.radio.isPlaying) }
    var currentSongIndex by remember { mutableStateOf(context.symphony.radio.queue.currentSongIndex) }
    var queueSize by remember { mutableStateOf(context.symphony.radio.queue.originalQueue.size) }
    var currentLoopMode by remember { mutableStateOf(context.symphony.radio.queue.currentLoopMode) }
    var currentShuffleMode by remember { mutableStateOf(context.symphony.radio.queue.currentShuffleMode) }
    var hasSleepTimer by remember { mutableStateOf(context.symphony.radio.hasSleepTimer()) }
    var showSongAdditionalInfo by remember {
        mutableStateOf(context.symphony.settings.getShowNowPlayingAdditionalInfo())
    }
    var enableSeekControls by remember {
        mutableStateOf(context.symphony.settings.getEnableSeekControls())
    }
    var seekBackDuration by remember {
        mutableStateOf(context.symphony.settings.getSeekBackDuration())
    }
    var seekForwardDuration by remember {
        mutableStateOf(context.symphony.settings.getSeekForwardDuration())
    }
    var isViable by remember { mutableStateOf(song != null) }

    BackHandler {
        context.navController.popBackStack()
    }

    EventerEffect(context.symphony.radio.onUpdate) {
        song = context.symphony.radio.queue.currentPlayingSong
        isPlaying = context.symphony.radio.isPlaying
        currentSongIndex = context.symphony.radio.queue.currentSongIndex
        queueSize = context.symphony.radio.queue.originalQueue.size
        currentLoopMode = context.symphony.radio.queue.currentLoopMode
        currentShuffleMode = context.symphony.radio.queue.currentShuffleMode
        hasSleepTimer = context.symphony.radio.hasSleepTimer()
        isViable = song != null
    }

    EventerEffect(context.symphony.settings.onChange) { key ->
        when (key) {
            SettingsKeys.showNowPlayingAdditionalInfo -> {
                showSongAdditionalInfo = context.symphony.settings.getShowNowPlayingAdditionalInfo()
            }
            SettingsKeys.enableSeekControls -> {
                enableSeekControls = context.symphony.settings.getEnableSeekControls()
            }
            SettingsKeys.seekBackDuration -> {
                seekBackDuration = context.symphony.settings.getSeekBackDuration()
            }
            SettingsKeys.seekForwardDuration -> {
                seekForwardDuration = context.symphony.settings.getSeekForwardDuration()
            }
            else -> {}
        }
    }

    when {
        isViable -> NowPlayingBody(
            context,
            PlayerStateData(
                song = song!!,
                isPlaying = isPlaying,
                currentSongIndex = currentSongIndex,
                queueSize = queueSize,
                currentLoopMode = currentLoopMode,
                currentShuffleMode = currentShuffleMode,
                hasSleepTimer = hasSleepTimer,
                showSongAdditionalInfo = showSongAdditionalInfo,
                enableSeekControls = enableSeekControls,
                seekBackDuration = seekBackDuration,
                seekForwardDuration = seekForwardDuration,
            )
        )
        else -> NothingPlaying(context)
    }
}

private val defaultHorizontalPadding = 20.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingAppBar(context: ViewContext) {
    CenterAlignedTopAppBar(
        title = {
            TopAppBarMinimalTitle {
                Text(context.symphony.t.nowPlaying)
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
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
        }
    )
}

@Composable
fun NowPlayingLandscapeAppBar(context: ViewContext) {
    Row(
        modifier = Modifier.padding(defaultHorizontalPadding, 4.dp, 12.dp, 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TopAppBarMinimalTitle {
            Text(context.symphony.t.nowPlaying)
        }
        Box(modifier = Modifier.weight(1f))
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NowPlayingBody(context: ViewContext, data: PlayerStateData) {
    data.run {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val orientation = ScreenOrientation.fromConstraints(this@BoxWithConstraints)
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    if (orientation.isPortrait) {
                        NowPlayingAppBar(context)
                    }
                },
                content = { contentPadding ->
                    BoxWithConstraints(modifier = Modifier.padding(contentPadding)) {
                        when (orientation) {
                            ScreenOrientation.PORTRAIT -> Column(modifier = Modifier.fillMaxSize()) {
                                Box(modifier = Modifier.weight(1f))
                                NowPlayingBodyCover(context, data)
                                Box(modifier = Modifier.weight(1f))
                                Column {
                                    NowPlayingBodyContent(context, data)
                                    NowPlayingBodyBottomBar(context, data)
                                }
                            }
                            ScreenOrientation.LANDSCAPE -> Row(modifier = Modifier.fillMaxSize()) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(0.dp, 12.dp)
                                ) {
                                    NowPlayingBodyCover(context, data)
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    Column {
                                        NowPlayingLandscapeAppBar(context)
                                        Box(modifier = Modifier.weight(1f))
                                        NowPlayingBodyContent(context, data)
                                        NowPlayingBodyBottomBar(context, data)
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun NowPlayingBodyCover(context: ViewContext, data: PlayerStateData) {
    data.run {
        BoxWithConstraints(modifier = Modifier.padding(defaultHorizontalPadding, 0.dp)) {
            val dimension = min(maxHeight, maxWidth)
            AsyncImage(
                song.createArtworkImageRequest(context.symphony).build(),
                null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(dimension)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NowPlayingBodyContent(context: ViewContext, data: PlayerStateData) {
    var isInFavorites by remember(data.song.id) {
        mutableStateOf(context.symphony.groove.playlist.isInFavorites(data.song.id))
    }

    EventerEffect(context.symphony.groove.playlist.onFavoritesUpdate) { favorites ->
        isInFavorites = favorites.contains(data.song.id)
    }

    data.run {
        Column {
            Row {
                Column(
                    modifier = Modifier
                        .padding(defaultHorizontalPadding, 0.dp)
                        .weight(1f)
                ) {
                    Text(
                        song.title,
                        style = MaterialTheme.typography.headlineSmall
                            .copy(fontWeight = FontWeight.Bold),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                    song.artistName?.let {
                        Text(
                            it,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    if (data.showSongAdditionalInfo) {
                        song.additional.toSamplingInfoString(context.symphony)?.let {
                            val localContentColor = LocalContentColor.current
                            Text(
                                it,
                                style = MaterialTheme.typography.labelSmall
                                    .copy(color = localContentColor.copy(alpha = 0.7f)),
                                modifier = Modifier.padding(top = 4.dp),
                            )
                        }
                    }
                }
                Row {
                    /*
                    IconButton(
                        modifier = Modifier.offset(4.dp),
                        onClick = {
                            context.symphony.groove.playlist.run {
                                when {
                                    isInFavorites -> removeFromFavorites(song.id)
                                    else -> addToFavorites(song.id)
                                }
                            }
                        }
                    ) {
                        when {
                            isInFavorites -> Icon(
                                Icons.Default.Favorite,
                                null,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                            else -> Icon(Icons.Default.FavoriteBorder, null)
                        }
                    }
                     */

                    var showOptionsMenu by remember { mutableStateOf(false) }
                    IconButton(
                        onClick = {
                            showOptionsMenu = !showOptionsMenu
                        }
                    ) {
                        Icon(Icons.Default.MoreVert, null)
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
            Spacer(modifier = Modifier.height(defaultHorizontalPadding + 8.dp))
            Row(
                modifier = Modifier.padding(defaultHorizontalPadding, 0.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                NowPlayingControlButton(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    color = MaterialTheme.colorScheme.onPrimary,
                    icon = when {
                        !isPlaying -> Icons.Default.PlayArrow
                        else -> Icons.Default.Pause
                    },
                    onClick = {
                        context.symphony.radio.shorty.playPause()
                    }
                )
                NowPlayingControlButton(
                    icon = Icons.Default.SkipPrevious,
                    onClick = {
                        context.symphony.radio.shorty.previous()
                    }
                )
                if (enableSeekControls) {
                    NowPlayingControlButton(
                        icon = Icons.Default.FastRewind,
                        onClick = {
                            context.symphony.radio.shorty
                                .seekFromCurrent(-seekBackDuration)
                        }
                    )
                    NowPlayingControlButton(
                        icon = Icons.Default.FastForward,
                        onClick = {
                            context.symphony.radio.shorty
                                .seekFromCurrent(seekForwardDuration)
                        }
                    )
                }
                NowPlayingControlButton(
                    icon = Icons.Default.SkipNext,
                    onClick = {
                        context.symphony.radio.shorty.skip()
                    }
                )
            }
            Spacer(modifier = Modifier.height(defaultHorizontalPadding))
            Row(
                modifier = Modifier.padding(defaultHorizontalPadding, 0.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val interactionSource = remember { MutableInteractionSource() }
                var sliderPosition by remember { mutableStateOf<Int?>(null) }
                var duration by remember {
                    mutableStateOf(
                        context.symphony.radio.currentPlaybackPosition
                            ?: PlaybackPosition.zero
                    )
                }
                EventerEffect(context.symphony.radio.onPlaybackPositionUpdate) {
                    duration = it
                }
                Text(
                    DurationFormatter.formatMs(sliderPosition ?: duration.played),
                    style = MaterialTheme.typography.labelMedium
                )
                BoxWithConstraints(modifier = Modifier.weight(1f)) {
                    Slider(
                        value = (sliderPosition ?: duration.played).toFloat(),
                        valueRange = 0f..duration.total.toFloat(),
                        onValueChange = {
                            sliderPosition = it.toInt()
                        },
                        onValueChangeFinished = {
                            sliderPosition?.let {
                                context.symphony.radio.seek(it)
                                sliderPosition = null
                            }
                        },
                        interactionSource = interactionSource,
                        thumb = {
                            SliderDefaults.Thumb(
                                interactionSource = interactionSource,
                                thumbSize = DpSize(12.dp, 12.dp),
                                // NOTE: pad top to fix stupid layout
                                modifier = Modifier.padding(top = 4.dp),
                            )
                        }
                    )
                }
                Text(
                    DurationFormatter.formatMs(duration.total),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
private fun NowPlayingControlButton(
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    color: Color = LocalContentColor.current,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier.background(
            backgroundColor,
            CircleShape
        ),
        onClick = onClick,
    ) {
        Icon(icon, null, tint = color)
    }
}

@Composable
private fun NowPlayingBodyBottomBar(context: ViewContext, data: PlayerStateData) {
    var showSleepTimer by remember { mutableStateOf(false) }

    data.run {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 4.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(
                onClick = {
                    context.navController.navigate(Routes.Queue)
                }
            ) {
                Icon(
                    Icons.Default.Sort,
                    null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    context.symphony.t.playingXofY(
                        currentSongIndex + 1,
                        queueSize
                    )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    showSleepTimer = !showSleepTimer
                }
            ) {
                Icon(
                    Icons.Outlined.Timer,
                    null,
                    tint = when {
                        hasSleepTimer -> MaterialTheme.colorScheme.primary
                        else -> LocalContentColor.current
                    }
                )
            }
            IconButton(
                onClick = {
                    context.symphony.radio.queue.toggleLoopMode()
                }
            ) {
                Icon(
                    when (currentLoopMode) {
                        RadioLoopMode.Song -> Icons.Default.RepeatOne
                        else -> Icons.Default.Repeat
                    },
                    null,
                    tint = when (currentLoopMode) {
                        RadioLoopMode.None -> LocalContentColor.current
                        else -> MaterialTheme.colorScheme.primary
                    }
                )
            }
            IconButton(
                onClick = {
                    context.symphony.radio.queue.toggleShuffleMode()
                }
            ) {
                Icon(
                    Icons.Default.Shuffle,
                    null,
                    tint = if (!currentShuffleMode) LocalContentColor.current
                    else MaterialTheme.colorScheme.primary
                )
            }
        }

        if (showSleepTimer) {
            when {
                hasSleepTimer -> context.symphony.radio.getSleepTimer()?.let {
                    NowPlayingSleepTimerDialog(
                        context,
                        sleepTimer = it,
                        onDismissRequest = {
                            showSleepTimer = false
                        }
                    )
                }
                else -> NowPlayingSleepTimerSetDialog(
                    context,
                    onDismissRequest = {
                        showSleepTimer = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NowPlayingSleepTimerDialog(
    context: ViewContext,
    sleepTimer: RadioSleepTimer,
    onDismissRequest: () -> Unit,
) {
    var updateTimer by remember { mutableStateOf<Timer?>(null) }
    val endsAtMs by remember { mutableStateOf(sleepTimer.endsAt) }
    var endsIn by remember { mutableStateOf(0L) }

    LaunchedEffect(LocalContext.current) {
        updateTimer = kotlin.concurrent.timer(period = 500L) {
            endsIn = endsAtMs - System.currentTimeMillis()
        }
    }

    DisposableEffect(LocalContext.current) {
        onDispose {
            updateTimer?.cancel()
            updateTimer = null
        }
    }

    ScaffoldDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(context.symphony.t.sleepTimer)
        },
        content = {
            Text(
                DurationFormatter.formatMs(endsIn),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 20.dp,
                        bottom = 12.dp,
                    ),
            )
        },
        actions = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                var quitOnEnd by remember { mutableStateOf(sleepTimer.quitOnEnd) }

                Checkbox(
                    checked = quitOnEnd,
                    onCheckedChange = {
                        quitOnEnd = it
                        sleepTimer.quitOnEnd = it
                    }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    context.symphony.t.quitAppOnEnd,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                onClick = {
                    context.symphony.radio.clearSleepTimer()
                    onDismissRequest()
                }
            ) {
                Text(context.symphony.t.stop)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NowPlayingSleepTimerSetDialog(
    context: ViewContext,
    onDismissRequest: () -> Unit,
) {
    val minDurationMs = Duration.ofMinutes(1).toMillis()
    val presetDurations = remember {
        listOf(0L to 15L, 0L to 30L, 1L to 0L, 2L to 0L, 3L to 0L)
    }
    var inputHours by remember { mutableStateOf(0L) }
    var inputMinutes by remember { mutableStateOf(10L) }
    var quitOnEnd by remember { mutableStateOf(false) }
    val inputDuration by remember {
        derivedStateOf {
            Duration
                .ofHours(inputHours)
                .plusMinutes(inputMinutes)
                .toMillis()
        }
    }
    val isValidDuration by remember {
        derivedStateOf { inputDuration >= minDurationMs }
    }

    ScaffoldDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(context.symphony.t.sleepTimer)
        },
        content = {
            Column(modifier = Modifier.padding(top = 12.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        4.dp,
                        Alignment.CenterHorizontally
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    presetDurations.forEach { x ->
                        val hours = x.first
                        val minutes = x.second
                        val shape = RoundedCornerShape(4.dp)

                        Text(
                            DurationFormatter.formatMinSec(hours, minutes),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    shape,
                                )
                                .clip(shape)
                                .clickable {
                                    inputHours = hours
                                    inputMinutes = minutes
                                }
                                .padding(8.dp, 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.padding(20.dp, 0.dp)) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = DividerDefaults.color,
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = {
                            Text(context.symphony.t.hours)
                        },
                        value = inputHours.toString(),
                        onValueChange = {
                            inputHours = it.toLongOrNull() ?: 0
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = DividerDefaults.color,
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = {
                            Text(context.symphony.t.minutes)
                        },
                        value = inputMinutes.toString(),
                        onValueChange = {
                            inputMinutes = it.toLongOrNull() ?: 0
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp, 0.dp),
                ) {
                    Checkbox(
                        checked = quitOnEnd,
                        onCheckedChange = {
                            quitOnEnd = it
                        }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(context.symphony.t.quitAppOnEnd)
                }
            }
        },
        actions = {
            TextButton(onClick = onDismissRequest) {
                Text(context.symphony.t.cancel)
            }
            TextButton(
                enabled = isValidDuration,
                onClick = {
                    context.symphony.radio.setSleepTimer(
                        duration = inputDuration,
                        quitOnEnd = quitOnEnd,
                    )
                    onDismissRequest()
                }
            ) {
                Text(context.symphony.t.done)
            }
        },
    )
}
