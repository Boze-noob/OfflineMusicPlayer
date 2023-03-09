package com.applid.musicbox.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.applid.musicbox.services.groove.Album
import com.applid.musicbox.services.groove.GrooveKinds
import com.applid.musicbox.ui.helpers.ViewContext

@Composable
fun AlbumRow(context: ViewContext, albums: List<Album>) {
    BoxWithConstraints {
        val maxSize = min(maxHeight, maxWidth).div(2f)
        val width = min(maxSize, 200.dp)

        LazyRow {
            itemsIndexed(
                albums.toList(),
                key = { i, x -> "$i-${x.id}" },
                contentType = { _, _ -> GrooveKinds.ALBUM }
            ) { _, album ->
                Box(modifier = Modifier.width(width)) {
                    AlbumTile(context, album)
                }
            }
        }
    }
}
