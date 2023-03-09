package com.applid.musicbox.services.groove

import android.content.ContentUris
import android.provider.MediaStore
import com.applid.musicbox.Symphony
import com.applid.musicbox.ui.helpers.Assets
import com.applid.musicbox.ui.helpers.createHandyImageRequest
import com.applid.musicbox.utils.*
import java.util.concurrent.ConcurrentHashMap

enum class AlbumSortBy {
    CUSTOM,
    ALBUM_NAME,
    ARTIST_NAME,
    TRACKS_COUNT,
}

class AlbumRepository(private val symphony: Symphony) {
    private val cached = ConcurrentHashMap<Long, Album>()
    var isUpdating = false
    val onUpdate = Eventer<Nothing?>()

    private val searcher = FuzzySearcher<Album>(
        options = listOf(
            FuzzySearchOption({ it.name }, 3),
            FuzzySearchOption({ it.artist })
        )
    )

    fun fetch() {
        if (isUpdating) return
        isUpdating = true
        onUpdate.dispatch(null)
        val cursor = symphony.applicationContext.contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Audio.Albums.ALBUM + " ASC"
        )
        try {
            val updateDispatcher = GrooveRepositoryUpdateDispatcher {
                onUpdate.dispatch(null)
            }
            cursor?.use {
                while (it.moveToNext()) {
                    kotlin
                        .runCatching { Album.fromCursor(it) }
                        .getOrNull()
                        ?.let { album ->
                            cached[album.id] = album
                            updateDispatcher.increment()
                        }
                }
            }
        } catch (err: Exception) {
            Logger.error("AlbumRepository", "fetch failed: $err")
        }
        isUpdating = false
        onUpdate.dispatch(null)
    }

    fun reset() {
        cached.clear()
        onUpdate.dispatch(null)
    }

    fun getDefaultAlbumArtworkUri() = Assets.getPlaceholderUri(symphony.applicationContext)

    fun getAlbumArtworkUri(albumId: Long) = ContentUris.withAppendedId(
        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
        albumId
    )

    fun createAlbumArtworkImageRequest(albumId: Long) = createHandyImageRequest(
        symphony.applicationContext,
        image = getAlbumArtworkUri(albumId),
        fallback = Assets.placeholderId,
    )

    fun getAll() = cached.values.toList()
    fun getAlbumWithId(albumId: Long) = cached[albumId]

    fun getAlbumOfArtist(artistName: String) = cached.values.find {
        it.artist == artistName
    }

    fun getAlbumsOfArtist(artistName: String) = cached.values.filter {
        it.artist == artistName
    }

    fun getAlbumsOfAlbumArtist(artistName: String) =
        symphony.groove.song.getAlbumIdsOfAlbumArtist(artistName)
            .mapNotNull { getAlbumWithId(it) }

    fun search(terms: String) = searcher.search(terms, getAll()).subListNonStrict(7)

    companion object {
        fun sort(songs: List<Album>, by: AlbumSortBy, reversed: Boolean): List<Album> {
            val sorted = when (by) {
                AlbumSortBy.CUSTOM -> songs.toList()
                AlbumSortBy.ALBUM_NAME -> songs.sortedBy { it.name }
                AlbumSortBy.ARTIST_NAME -> songs.sortedBy { it.artist }
                AlbumSortBy.TRACKS_COUNT -> songs.sortedBy { it.numberOfTracks }
            }
            return if (reversed) sorted.reversed() else sorted
        }
    }
}
