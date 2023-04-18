package com.applid.musicbox.ui.view


import android.app.Activity
import android.content.ActivityNotFoundException
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.with
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.applid.musicbox.ads.HOME_INTERSTITIAL_AD_UNIT
import com.applid.musicbox.ads.InterstitialAdHelper
import com.applid.musicbox.data.GooglePlayUrl
import com.applid.musicbox.data.GooglePlayUrlWeb
import com.applid.musicbox.ui.components.NowPlayingBottomBar
import com.applid.musicbox.ui.components.RateUsDialog
import com.applid.musicbox.ui.components.TopAppBarMinimalTitle
import com.applid.musicbox.ui.helpers.*
import com.applid.musicbox.ui.view.home.*

enum class HomePages(
    val label: (context: ViewContext) -> String,
    val selectedIcon: @Composable () -> ImageVector,
    val unselectedIcon: @Composable () -> ImageVector,
) {
    ForYou(
        label = { it.symphony.t.forYou },
        selectedIcon = { Icons.Filled.Face },
        unselectedIcon = { Icons.Outlined.Face }
    ),
    Songs(
        label = { it.symphony.t.songs },
        selectedIcon = { Icons.Filled.MusicNote },
        unselectedIcon = { Icons.Outlined.MusicNote }
    ),
    Artists(
        label = { it.symphony.t.artists },
        selectedIcon = { Icons.Filled.Group },
        unselectedIcon = { Icons.Outlined.Group }
    ),
    Albums(
        label = { it.symphony.t.albums },
        selectedIcon = { Icons.Filled.Album },
        unselectedIcon = { Icons.Outlined.Album }
    ),
    AlbumArtists(
        label = { it.symphony.t.albumArtists },
        selectedIcon = { Icons.Filled.SupervisorAccount },
        unselectedIcon = { Icons.Outlined.SupervisorAccount }
    ),
    Genres(
        label = { it.symphony.t.genres },
        selectedIcon = { Icons.Filled.Tune },
        unselectedIcon = { Icons.Outlined.Tune }
    ),
    Folders(
        label = { it.symphony.t.folders },
        selectedIcon = { Icons.Filled.Folder },
        unselectedIcon = { Icons.Outlined.Folder }
    ),
    Playlists(
        label = { it.symphony.t.playlists },
        selectedIcon = { Icons.Filled.QueueMusic },
        unselectedIcon = { Icons.Outlined.QueueMusic }
    ),
    Tree(
        label = { it.symphony.t.tree },
        selectedIcon = { Icons.Filled.AccountTree },
        unselectedIcon = { Icons.Outlined.AccountTree }
    );
}

enum class HomePageBottomBarLabelVisibility {
    ALWAYS_VISIBLE,
    VISIBLE_WHEN_ACTIVE,
    INVISIBLE,
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeView(context: ViewContext) {
    val currentContext = LocalContext.current
    var showRateUs by remember {
        mutableStateOf(
            false
        )
    }

    val tabs = context.symphony.settings.getHomeTabs().toList()
    val labelVisibility = context.symphony.settings.getHomePageBottomBarLabelVisibility()
    var currentPage by remember {
        mutableStateOf(context.symphony.settings.getHomeLastTab())
    }
    var showOptionsDropdown by remember { mutableStateOf(false) }
    val data = remember { HomeViewData(context.symphony) }
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(LocalContext.current) {
        data.initialize()
    }

    LaunchedEffect(Unit) {
        if(!showRateUs && (0..10).random() < 2) {
            showRateUs = !context.symphony.settings.getRateUs()
        }
    }

    DisposableEffect(LocalContext.current) {
        onDispose { data.dispose() }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                navigationIcon = {
                    IconButton(
                        content = {
                            Icon(Icons.Default.Search, null)
                        },
                        onClick = {
                            context.navController.navigate(Routes.Search)
                        }
                    )
                },
                title = {
                    Crossfade(targetState = currentPage.label(context)) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            TopAppBarMinimalTitle { Text(it) }
                        }
                    }
                },
                actions = {
                    IconButton(
                        content = {
                            Icon(Icons.Default.MoreVert, null)
                            DropdownMenu(
                                expanded = showOptionsDropdown,
                                onDismissRequest = { showOptionsDropdown = false },
                            ) {
                                DropdownMenuItem(
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Settings,
                                            context.symphony.t.settings
                                        )
                                    },
                                    text = {
                                        Text(context.symphony.t.settings)
                                    },
                                    onClick = {
                                        showOptionsDropdown = false
                                        context.navController.navigate(Routes.Settings)
                                    }
                                )
                            }
                        },
                        onClick = {
                            showOptionsDropdown = !showOptionsDropdown
                        }
                    )
                }
            )
        },
        content = { contentPadding ->
            AnimatedContent(
                targetState = currentPage,
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                transitionSpec = {
                    SlideTransition.slideUp.enterTransition()
                        .with(ScaleTransition.scaleDown.exitTransition())
                },
            ) { page ->
                when (page) {
                    HomePages.ForYou -> ForYouView(context, data)
                    HomePages.Songs -> SongsView(context, data)
                    HomePages.Albums -> AlbumsView(context, data)
                    HomePages.Artists -> ArtistsView(context, data)
                    HomePages.AlbumArtists -> AlbumArtistsView(context, data)
                    HomePages.Genres -> GenresView(context, data)
                    HomePages.Folders -> FoldersView(context, data)
                    HomePages.Playlists -> PlaylistsView(context, data)
                    HomePages.Tree -> TreeView(context, data)
                }
            }
        },
        bottomBar = {
            Column {
                NowPlayingBottomBar(context)
                NavigationBar {
                    Spacer(modifier = Modifier.width(2.dp))
                    tabs.map { page ->
                        val isSelected = currentPage == page
                        val label = page.label(context)
                        NavigationBarItem(
                            selected = isSelected,
                            alwaysShowLabel = labelVisibility == HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE,
                            icon = {
                                Crossfade(targetState = isSelected) {
                                    Icon(
                                        if (it) page.selectedIcon() else page.unselectedIcon(),
                                        label,
                                    )
                                }
                            },
                            label = when (labelVisibility) {
                                HomePageBottomBarLabelVisibility.INVISIBLE -> null
                                else -> ({
                                    Text(
                                        label,
                                        style = MaterialTheme.typography.labelSmall,
                                        textAlign = TextAlign.Center,
                                        overflow = TextOverflow.Ellipsis,
                                        softWrap = false,
                                    )
                                })
                            },
                            onClick = {
                                currentPage = page
                                context.symphony.settings.setHomeLastTab(currentPage)
                                if(!showRateUs && (0..10).random() < 6
                                ) {
                                    InterstitialAdHelper().get(currentContext, HOME_INTERSTITIAL_AD_UNIT) {
                                        it?.show(currentContext as Activity)
                                    }
                                }


                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(2.dp))
                }
            }
        }
    )

    if (showRateUs) {
            RateUsDialog(
                context,
                onDismissRequest = {
                    showRateUs = false
                    if(it) {
                        try {
                            uriHandler.openUri(GooglePlayUrl)
                        } catch (e: ActivityNotFoundException) {
                            uriHandler.openUri(GooglePlayUrlWeb)
                        }
                        context.symphony.settings.setRateUs(true)
                    }
                },
            )
        }
}
