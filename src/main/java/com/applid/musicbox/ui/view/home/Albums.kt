package com.applid.musicbox.ui.view.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.applid.musicbox.ui.components.AlbumGrid
import com.applid.musicbox.ui.components.IconTextBody
import com.applid.musicbox.ui.components.LoaderScaffold
import com.applid.musicbox.ui.helpers.ViewContext

@Composable
fun AlbumsView(context: ViewContext, data: HomeViewData) {
    LoaderScaffold(context, isLoading = data.albumsIsUpdating) {
        when {
            data.albums.isNotEmpty() -> AlbumGrid(
                context,
                albums = data.albums,
            )
            else -> IconTextBody(
                icon = { modifier ->
                    Icon(
                        Icons.Default.Album,
                        null,
                        modifier = modifier,
                    )
                },
                content = { Text(context.symphony.t.damnThisIsSoEmpty) }
            )
        }
    }
}
