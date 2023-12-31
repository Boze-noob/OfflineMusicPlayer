package com.applid.musicbox.ui.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.applid.musicbox.ui.helpers.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.applid.musicbox.MainActivity
import com.applid.musicbox.Symphony
import com.applid.musicbox.ui.theme.SymphonyTheme


@Composable
fun BaseView(symphony: Symphony, activity: MainActivity) {
    val context = ViewContext(
        symphony = symphony,
        activity = activity,
        navController = rememberNavController(),
    )

    SymphonyTheme(context) {
        Surface(color = MaterialTheme.colorScheme.background) {
            NavHost(
                navController = context.navController,
                startDestination = Routes.Home.route
            ) {
                composable(
                    Routes.Home.route,
                    popEnterTransition = { FadeTransition.enterTransition() },
                ) {
                    HomeView(context)
                }
                composable(
                    Routes.NowPlaying.route,
                    enterTransition = { SlideTransition.slideUp.enterTransition() },
                    exitTransition = { FadeTransition.exitTransition() },
                    popEnterTransition = { FadeTransition.enterTransition() },
                    popExitTransition = { SlideTransition.slideDown.exitTransition() },
                ) {
                    NowPlayingView(context)
                }
                composable(
                    Routes.Queue.route,
                    enterTransition = { SlideTransition.slideUp.enterTransition() },
                    exitTransition = { SlideTransition.slideDown.exitTransition() },
                ) {
                    QueueView(context)
                }
                composable(
                    Routes.Settings.route,
                    enterTransition = { ScaleTransition.scaleDown.enterTransition() },
                    exitTransition = { ScaleTransition.scaleUp.exitTransition() },
                ) {
                    SettingsView(context)
                }
                composable(
                    Routes.Artist.route,
                    enterTransition = { SlideTransition.slideLeft.enterTransition() },
                    exitTransition = { SlideTransition.slideLeft.exitTransition() },
                ) { backStackEntry ->
                    ArtistView(
                        context,
                        backStackEntry.getRouteArgument(RoutesParameters.ArtistRouteArtistName)
                            ?: ""
                    )
                }
                composable(
                    Routes.Album.route,
                    enterTransition = { SlideTransition.slideLeft.enterTransition() },
                    exitTransition = { SlideTransition.slideLeft.exitTransition() },
                ) { backStackEntry ->
                    AlbumView(
                        context,
                        backStackEntry.getRouteArgument(RoutesParameters.AlbumRouteAlbumId)
                            ?.toLongOrNull() ?: -1
                    )
                }
                composable(
                    Routes.Search.route,
                    enterTransition = { SlideTransition.slideDown.enterTransition() },
                    exitTransition = { SlideTransition.slideUp.exitTransition() },
                ) {
                    SearchView(context)
                }
                composable(
                    Routes.AlbumArtist.route,
                    enterTransition = { SlideTransition.slideLeft.enterTransition() },
                    exitTransition = { SlideTransition.slideLeft.exitTransition() },
                ) { backStackEntry ->
                    AlbumArtistView(
                        context,
                        backStackEntry.getRouteArgument(RoutesParameters.AlbumArtistRouteArtistName)
                            ?: ""
                    )
                }
                composable(
                    Routes.Genre.route,
                    enterTransition = { SlideTransition.slideLeft.enterTransition() },
                    exitTransition = { SlideTransition.slideLeft.exitTransition() },
                ) { backStackEntry ->
                    GenreView(
                        context,
                        backStackEntry.getRouteArgument(RoutesParameters.GenreRouteGenre)
                            ?: ""
                    )
                }
                composable(
                    Routes.Playlist.route,
                    enterTransition = { SlideTransition.slideLeft.enterTransition() },
                    exitTransition = { SlideTransition.slideLeft.exitTransition() },
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
