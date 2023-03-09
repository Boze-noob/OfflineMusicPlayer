package com.applid.musicbox.ui.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import com.applid.musicbox.data.ContactMail
import com.applid.musicbox.data.OurMarketPlaceUrl
import com.applid.musicbox.services.SettingsDataDefaults
import com.applid.musicbox.services.ThemeMode
import com.applid.musicbox.services.i18n.Translations
import com.applid.musicbox.ui.components.AdaptiveSnackbar
import com.applid.musicbox.ui.components.EventerEffect
import com.applid.musicbox.ui.components.TopAppBarMinimalTitle
import com.applid.musicbox.ui.helpers.ViewContext
import com.applid.musicbox.ui.theme.PrimaryThemeColors
import com.applid.musicbox.ui.theme.ThemeColors
import com.applid.musicbox.ui.view.settings.*
import io.github.zyrouge.symphony.ui.view.settings.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(context: ViewContext) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var settings by remember { mutableStateOf(context.symphony.settings.getSettings()) }
    val uriHandler = LocalUriHandler.current
    val localContext = LocalContext.current

    val refetchLibrary = {
        coroutineScope.launch {
            context.symphony.groove.refetch()
        }
    }

    EventerEffect(context.symphony.settings.onChange) {
        settings = context.symphony.settings.getSettings()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                AdaptiveSnackbar(it)
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TopAppBarMinimalTitle {
                        Text(context.symphony.t.settings)
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
                        Icon(Icons.Default.ArrowBack, null)
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
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    SettingsSideHeading(context.symphony.t.appearance)
                    SettingsOptionTile(
                        icon = {
                            Icon(Icons.Default.Language, null)
                        },
                        title = {
                            Text(context.symphony.t.language_)
                        },
                        value = settings.language ?: context.symphony.t.language,
                        values = Translations.all.associate {
                            it.language to it.language
                        },
                        onChange = { value ->
                            context.symphony.settings.setLanguage(value)
                        }
                    )
                    SettingsOptionTile(
                        icon = {
                            Icon(Icons.Default.Palette, null)
                        },
                        title = {
                            Text(context.symphony.t.theme)
                        },
                        value = settings.themeMode,
                        values = mapOf(
                            ThemeMode.SYSTEM to context.symphony.t.system,
                            ThemeMode.LIGHT to context.symphony.t.light,
                            ThemeMode.DARK to context.symphony.t.dark,
                            ThemeMode.BLACK to context.symphony.t.black,
                        ),
                        onChange = { value ->
                            context.symphony.settings.setThemeMode(value)
                        }
                    )
                    SettingsSwitchTile(
                        icon = {
                            Icon(Icons.Default.Face, null)
                        },
                        title = {
                            Text(context.symphony.t.materialYou)
                        },
                        value = settings.useMaterialYou,
                        onChange = { value ->
                            context.symphony.settings.setUseMaterialYou(value)
                        }
                    )
                    SettingsOptionTile(
                        icon = {
                            Icon(Icons.Default.Colorize, null)
                        },
                        title = {
                            Text(context.symphony.t.primaryColor)
                        },
                        value = ThemeColors.resolvePrimaryColorKey(settings.primaryColor),
                        values = PrimaryThemeColors.values()
                            .associateWith { it.toHumanString() },
                        onChange = { value ->
                            context.symphony.settings.setPrimaryColor(value.name)
                        }
                    )
                    SettingsMultiOptionTile(
                        context,
                        icon = {
                            Icon(Icons.Default.Home, null)
                        },
                        title = {
                            Text(context.symphony.t.homeTabs)
                        },
                        note = {
                            Text(context.symphony.t.selectAtleast2orAtmost5Tabs)
                        },
                        value = settings.homeTabs,
                        values = HomePages.values().associateWith { it.label(context) },
                        satisfies = { it.size in 2..5 },
                        onChange = { value ->
                            context.symphony.settings.setHomeTabs(value)
                        }
                    )
                    SettingsOptionTile(
                        icon = {
                            Icon(Icons.Default.Label, null)
                        },
                        title = {
                            Text(context.symphony.t.bottomBarLabelVisibility)
                        },
                        value = settings.homePageBottomBarLabelVisibility,
                        values = HomePageBottomBarLabelVisibility.values()
                            .associateWith { it.label(context) },
                        onChange = { value ->
                            context.symphony.settings.setHomePageBottomBarLabelVisibility(value)
                        }
                    )
                    SettingsSwitchTile(
                        icon = {
                            Icon(Icons.Default.SkipNext, null)
                        },
                        title = {
                            Text(context.symphony.t.miniPlayerTrackControls)
                        },
                        value = settings.miniPlayerTrackControls,
                        onChange = { value ->
                            context.symphony.settings.setMiniPlayerTrackControls(value)
                        }
                    )
                    SettingsSwitchTile(
                        icon = {
                            Icon(Icons.Default.Forward30, null)
                        },
                        title = {
                            Text(context.symphony.t.miniPlayerSeekControls)
                        },
                        value = settings.miniPlayerSeekControls,
                        onChange = { value ->
                            context.symphony.settings.setMiniPlayerSeekControls(value)
                        }
                    )
                    Divider()
                    SettingsSideHeading(context.symphony.t.player)
                    SettingsSwitchTile(
                        icon = {
                            Icon(Icons.Default.GraphicEq, null)
                        },
                        title = {
                            Text(context.symphony.t.fadePlaybackInOut)
                        },
                        value = settings.fadePlayback,
                        onChange = { value ->
                            context.symphony.settings.setFadePlayback(value)
                        }
                    )
                    SettingsSliderTile(
                        context,
                        icon = {
                            Icon(Icons.Default.GraphicEq, null)
                        },
                        title = {
                            Text(context.symphony.t.fadePlaybackInOut)
                        },
                        label = { value ->
                            Text(context.symphony.t.XSecs(value))
                        },
                        range = 0.5f..6f,
                        initialValue = settings.fadePlaybackDuration,
                        onValue = { value ->
                            value.times(2).roundToInt().toFloat().div(2)
                        },
                        onChange = { value ->
                            context.symphony.settings.setFadePlaybackDuration(value)
                        },
                        onReset = {
                            context.symphony.settings.setFadePlaybackDuration(
                                SettingsDataDefaults.fadePlaybackDuration
                            )
                        },
                    )
                    SettingsSwitchTile(
                        icon = {
                            Icon(Icons.Default.CenterFocusWeak, null)
                        },
                        title = {
                            Text(context.symphony.t.requireAudioFocus)
                        },
                        value = settings.requireAudioFocus,
                        onChange = { value ->
                            context.symphony.settings.setRequireAudioFocus(value)
                        }
                    )
                    SettingsSwitchTile(
                        icon = {
                            Icon(Icons.Default.CenterFocusWeak, null)
                        },
                        title = {
                            Text(context.symphony.t.ignoreAudioFocusLoss)
                        },
                        value = settings.ignoreAudioFocusLoss,
                        onChange = { value ->
                            context.symphony.settings.setIgnoreAudioFocusLoss(value)
                        }
                    )
                    SettingsSwitchTile(
                        icon = {
                            Icon(Icons.Default.Headset, null)
                        },
                        title = {
                            Text(context.symphony.t.playOnHeadphonesConnect)
                        },
                        value = settings.playOnHeadphonesConnect,
                        onChange = { value ->
                            context.symphony.settings.setPlayOnHeadphonesConnect(value)
                        }
                    )
                    SettingsSwitchTile(
                        icon = {
                            Icon(Icons.Default.HeadsetOff, null)
                        },
                        title = {
                            Text(context.symphony.t.pauseOnHeadphonesDisconnect)
                        },
                        value = settings.pauseOnHeadphonesDisconnect,
                        onChange = { value ->
                            context.symphony.settings.setPauseOnHeadphonesDisconnect(value)
                        }
                    )
                    SettingsSwitchTile(
                        icon = {
                            Icon(Icons.Default.Wysiwyg, null)
                        },
                        title = {
                            Text(context.symphony.t.showAudioInformation)
                        },
                        value = settings.showNowPlayingAdditionalInfo,
                        onChange = { value ->
                            context.symphony.settings.setShowNowPlayingAdditionalInfo(value)
                        }
                    )
                    SettingsSwitchTile(
                        icon = {
                            Icon(Icons.Default.Forward30, null)
                        },
                        title = {
                            Text(context.symphony.t.enableSeekControls)
                        },
                        value = settings.enableSeekControls,
                        onChange = { value ->
                            context.symphony.settings.setEnableSeekControls(value)
                        }
                    )
                    val seekDurationRange = 3f..60f
                    SettingsSliderTile(
                        context,
                        icon = {
                            Icon(Icons.Default.FastRewind, null)
                        },
                        title = {
                            Text(context.symphony.t.fastRewindDuration)
                        },
                        label = { value ->
                            Text(context.symphony.t.XSecs(value))
                        },
                        range = seekDurationRange,
                        initialValue = settings.seekBackDuration.toFloat(),
                        onValue = { value ->
                            value.roundToInt().toFloat()
                        },
                        onChange = { value ->
                            context.symphony.settings.setSeekBackDuration(value.toInt())
                        },
                        onReset = {
                            context.symphony.settings.setSeekBackDuration(
                                SettingsDataDefaults.seekBackDuration
                            )
                        },
                    )
                    SettingsSliderTile(
                        context,
                        icon = {
                            Icon(Icons.Default.FastRewind, null)
                        },
                        title = {
                            Text(context.symphony.t.fastForwardDuration)
                        },
                        label = { value ->
                            Text(context.symphony.t.XSecs(value))
                        },
                        range = seekDurationRange,
                        initialValue = settings.seekForwardDuration.toFloat(),
                        onValue = { value ->
                            value.roundToInt().toFloat()
                        },
                        onChange = { value ->
                            context.symphony.settings.setSeekForwardDuration(value.toInt())
                        },
                        onReset = {
                            context.symphony.settings.setSeekForwardDuration(
                                SettingsDataDefaults.seekForwardDuration
                            )
                        },
                    )
                    Divider()
                    SettingsSideHeading(context.symphony.t.groove)
                    val defaultSongsFilterPattern = ".*"
                    SettingsTextInputTile(
                        context,
                        icon = {
                            Icon(Icons.Default.FilterAlt, null)
                        },
                        title = {
                            Text(context.symphony.t.songsFilterPattern)
                        },
                        value = settings.songsFilterPattern ?: defaultSongsFilterPattern,
                        onReset = {
                            context.symphony.settings.setSongsFilterPattern(null)
                        },
                        onChange = { value ->
                            context.symphony.settings.setSongsFilterPattern(
                                when (value) {
                                    defaultSongsFilterPattern -> null
                                    else -> value
                                }
                            )
                            refetchLibrary()
                        }
                    )
                    SettingsMultiFolderTile(
                        context,
                        icon = {
                            Icon(Icons.Default.RuleFolder, null)
                        },
                        title = {
                            Text(context.symphony.t.blacklistFolders)
                        },
                        explorer = context.symphony.groove.song.foldersExplorer,
                        initialValues = settings.blacklistFolders,
                        onChange = { values ->
                            context.symphony.settings.setBlacklistFolders(values)
                            refetchLibrary()
                        }
                    )
                    SettingsMultiFolderTile(
                        context,
                        icon = {
                            Icon(Icons.Default.RuleFolder, null)
                        },
                        title = {
                            Text(context.symphony.t.whitelistFolders)
                        },
                        explorer = context.symphony.groove.song.foldersExplorer,
                        initialValues = settings.whitelistFolders,
                        onChange = { values ->
                            context.symphony.settings.setWhitelistFolders(values)
                            refetchLibrary()
                        }
                    )
                    SettingsSimpleTile(
                        icon = {
                            Icon(Icons.Default.Storage, null)
                        },
                        title = {
                            Text(context.symphony.t.clearSongCache)
                        },
                        onClick = {
                            coroutineScope.launch {
                                context.symphony.database.songCache.update(mapOf())
                                refetchLibrary()
                                snackbarHostState.showSnackbar(
                                    context.symphony.t.songCacheCleared,
                                    withDismissAction = true,
                                )
                            }
                        }
                    )
                    Divider()
                    SettingsSideHeading(context.symphony.t.info)
                    AboutTile(
                        context = context,
                        icon = {
                            Icon(Icons.Default.Info, null)
                        },
                        title = {
                            Text(context.symphony.t.about)
                        },
                        dialogContent = context.symphony.t.aboutUsContent

                    )
                    SettingsSimpleTile(
                        icon = { Icon(Icons.Default.Apps, null) },
                        title = { Text(context.symphony.t.otherApps) },
                        onClick = {

                            uriHandler.openUri(OurMarketPlaceUrl)
                        }
                    )
                    SettingsSimpleTile(
                        icon = { Icon(Icons.Default.ContactMail, null) },
                        title = { Text(context.symphony.t.contactUs) },
                        onClick = {
                            contactUs(localContext, ContactMail, "MusicBox")
                        })
                }
            }
        }
    )
}

fun HomePageBottomBarLabelVisibility.label(context: ViewContext): String {
    return when (this) {
        HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE -> context.symphony.t.alwaysVisible
        HomePageBottomBarLabelVisibility.VISIBLE_WHEN_ACTIVE -> context.symphony.t.visibleWhenActive
        HomePageBottomBarLabelVisibility.INVISIBLE -> context.symphony.t.invisible
    }
}

fun contactUs(context: Context, emailAddress: String, subject: String) {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:$emailAddress")
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(Intent.createChooser(intent, "Send email"))
}
