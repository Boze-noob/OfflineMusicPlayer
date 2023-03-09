package com.applid.musicbox.ui.view.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.applid.musicbox.ui.components.GenreGrid
import com.applid.musicbox.ui.components.IconTextBody
import com.applid.musicbox.ui.components.LoaderScaffold
import com.applid.musicbox.ui.helpers.ViewContext

@Composable
fun GenresView(context: ViewContext, data: HomeViewData) {
    LoaderScaffold(context, isLoading = data.genresIsUpdating) {
        when {
            data.genres.isNotEmpty() -> GenreGrid(
                context,
                genres = data.genres,
            )
            else -> IconTextBody(
                icon = { modifier ->
                    Icon(
                        Icons.Default.MusicNote,
                        null,
                        modifier = modifier,
                    )
                },
                content = { Text(context.symphony.t.damnThisIsSoEmpty) }
            )
        }
    }
}
