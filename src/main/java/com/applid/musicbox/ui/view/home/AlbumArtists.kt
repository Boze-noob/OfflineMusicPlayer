package com.applid.musicbox.ui.view.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.applid.musicbox.ui.components.AlbumArtistGrid
import com.applid.musicbox.ui.components.IconTextBody
import com.applid.musicbox.ui.components.LoaderScaffold
import com.applid.musicbox.ui.helpers.ViewContext

@Composable
fun AlbumArtistsView(context: ViewContext, data: HomeViewData) {
    LoaderScaffold(context, isLoading = data.albumArtistsIsUpdating) {
        when {
            data.albumArtists.isNotEmpty() -> AlbumArtistGrid(
                context,
                artists = data.albumArtists,
            )
            else -> IconTextBody(
                icon = { modifier ->
                    Icon(
                        Icons.Default.Person,
                        null,
                        modifier = modifier,
                    )
                },
                content = { Text(context.symphony.t.damnThisIsSoEmpty) }
            )
        }
    }
}
