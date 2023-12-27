package com.applid.musicbox.ui.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.applid.musicbox.ui.helpers.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.applid.musicbox.MainActivity
import com.applid.musicbox.Symphony
import com.applid.musicbox.ui.theme.SymphonyTheme
//TODO fix this whole file
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BaseView(symphony: Symphony, activity: MainActivity) {
    val context = ViewContext(
        symphony = symphony,
        activity = activity,
        navController = rememberAnimatedNavController(),
    )

    SymphonyTheme(context) {
        Surface(color = MaterialTheme.colorScheme.background) {
            AnimatedNavHost(
                navController = context.navController,
                startDestination = Routes.Home.route
            ) {
                composable(
                    Routes.Home.route,
                    //TODO add transition popEnterTransition = { FadeTransition.enterTransition() },
                ) {
                    HomeView(context)
                }
                composable(
                    Routes.NowPlaying.route,
                    //TODO add transition enterTransition = { SlideTransition.slideUp.enterTransition() },
                    //TODO add transition exitTransition = { FadeTransition.exitTransition() },
                    //TODO add transition popEnterTransition = { FadeTransition.enterTransition() },
                    //TODO add transition popExitTransition = { SlideTransition.slideDown.exitTransition() },
                ) {
                    NowPlayingView(context)
                }
                composable(
                    Routes.Queue.route,
                    //TODO add transition enterTransition = { SlideTransition.slideUp.enterTransition() },
                    //TODO add transition exitTransition = { SlideTransition.slideDown.exitTransition() },
                ) {
                    QueueView(context)
                }
                composable(
                    Routes.Settings.route,
                    //TODO add transition enterTransition = { ScaleTransition.scaleDown.enterTransition() },
                    //TODO add transition exitTransition = { ScaleTransition.scaleUp.exitTransition() },
                ) {
                    SettingsView(context)
                }
                composable(
                    Routes.Artist.route,
                    //TODO add transition enterTransition = { SlideTransition.slideLeft.enterTransition() },
                    //TODO add transition exitTransition = { SlideTransition.slideLeft.exitTransition() },
                ) { backStackEntry ->
                    ArtistView(
                        context,
                        backStackEntry.getRouteArgument(RoutesParameters.ArtistRouteArtistName)
                            ?: ""
                    )
                }
                composable(
                    Routes.Album.route,
                    //TODO add transition enterTransition = { SlideTransition.slideLeft.enterTransition() },
                    //TODO add transition exitTransition = { SlideTransition.slideLeft.exitTransition() },
                ) { backStackEntry ->
                    AlbumView(
                        context,
                        backStackEntry.getRouteArgument(RoutesParameters.AlbumRouteAlbumId)
                            ?.toLongOrNull() ?: -1
                    )
                }
                composable(
                    Routes.Search.route,
                    //TODO add transition enterTransition = { SlideTransition.slideDown.enterTransition() },
                    //TODO add transition exitTransition = { SlideTransition.slideUp.exitTransition() },
                ) {
                    SearchView(context)
                }
                composable(
                    Routes.AlbumArtist.route,
                    //TODO add transition enterTransition = { SlideTransition.slideLeft.enterTransition() },
                    //TODO add transition exitTransition = { SlideTransition.slideLeft.exitTransition() },
                ) { backStackEntry ->
                    AlbumArtistView(
                        context,
                        backStackEntry.getRouteArgument(RoutesParameters.AlbumArtistRouteArtistName)
                            ?: ""
                    )
                }
                composable(
                    Routes.Genre.route,
                    //TODO add transition enterTransition = { SlideTransition.slideLeft.enterTransition() },
                    //TODO add transition exitTransition = { SlideTransition.slideLeft.exitTransition() },
                ) { backStackEntry ->
                    GenreView(
                        context,
                        backStackEntry.getRouteArgument(RoutesParameters.GenreRouteGenre)
                            ?: ""
                    )
                }
                composable(
                    Routes.Playlist.route,
                    //TODO add transition enterTransition = { SlideTransition.slideLeft.enterTransition() },
                    //TODO add transition exitTransition = { SlideTransition.slideLeft.exitTransition() },
                ) { backStackEntry ->
                    PlaylistView(
                        context,
                        backStackEntry.getRouteArgument(RoutesParameters.PlaylistRoutePlaylistId)
                            ?: ""
                    )
                }
            }
        }
    }
}
