package com.applid.musicbox.ui.view.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import com.applid.musicbox.Symphony
import com.applid.musicbox.utils.EventUnsubscribeFn
import com.applid.musicbox.utils.swap

class HomeViewData(val symphony: Symphony) {
    var songsIsUpdating by mutableStateOf(symphony.groove.song.isUpdating)
    val songs = symphony.groove.song.getAll().toMutableStateList()
    var songsExplorerId by mutableStateOf(System.currentTimeMillis())

    var artistsIsUpdating by mutableStateOf(symphony.groove.artist.isUpdating)
    val artists = symphony.groove.artist.getAll().toMutableStateList()

    var albumsIsUpdating by mutableStateOf(symphony.groove.album.isUpdating)
    val albums = symphony.groove.album.getAll().toMutableStateList()

    var albumArtistsIsUpdating by mutableStateOf(symphony.groove.albumArtist.isUpdating)
    val albumArtists = symphony.groove.albumArtist.getAll().toMutableStateList()

    var genresIsUpdating by mutableStateOf(symphony.groove.genre.isUpdating)
    val genres = symphony.groove.genre.getAll().toMutableStateList()

    var playlistsIsUpdating by mutableStateOf(symphony.groove.playlist.isUpdating)
    val playlists = symphony.groove.playlist.getAll().toMutableStateList()

    private var songsSubscriber: EventUnsubscribeFn? = null
    private var artistsSubscriber: EventUnsubscribeFn? = null
    private var albumsSubscriber: EventUnsubscribeFn? = null
    private var albumArtistsSubscriber: EventUnsubscribeFn? = null
    private var genresSubscriber: EventUnsubscribeFn? = null
    private var playlistsSubscriber: EventUnsubscribeFn? = null

    fun initialize() {
        updateAllStates()
        songsSubscriber = symphony.groove.song.onUpdate.subscribe { updateSongsState() }
        artistsSubscriber = symphony.groove.artist.onUpdate.subscribe { updateArtistsState() }
        albumsSubscriber = symphony.groove.album.onUpdate.subscribe { updateAlbumsState() }
        albumArtistsSubscriber =
            symphony.groove.albumArtist.onUpdate.subscribe { updateAlbumArtistsState() }
        genresSubscriber = symphony.groove.genre.onUpdate.subscribe { updateGenresState() }
        playlistsSubscriber = symphony.groove.playlist.onUpdate.subscribe { updatePlaylistsState() }
    }

    fun dispose() {
        songsSubscriber?.invoke()
        artistsSubscriber?.invoke()
        albumsSubscriber?.invoke()
        albumArtistsSubscriber?.invoke()
        genresSubscriber?.invoke()
        playlistsSubscriber?.invoke()
    }

    private fun updateAllStates() {
        updateSongsState()
        updateArtistsState()
        updateAlbumsState()
        updateAlbumArtistsState()
        updateGenresState()
        updatePlaylistsState()
    }

    private fun updateSongsState() {
        songsIsUpdating = symphony.groove.song.isUpdating
        songs.swap(symphony.groove.song.getAll())
        songsExplorerId = System.currentTimeMillis()
    }

    private fun updateArtistsState() {
        artistsIsUpdating = symphony.groove.artist.isUpdating
        artists.swap(symphony.groove.artist.getAll())
    }

    private fun updateAlbumsState() {
        albumsIsUpdating = symphony.groove.album.isUpdating
        albums.swap(symphony.groove.album.getAll())
    }

    private fun updateAlbumArtistsState() {
        albumArtistsIsUpdating = symphony.groove.albumArtist.isUpdating
        albumArtists.swap(symphony.groove.albumArtist.getAll())
    }

    private fun updateGenresState() {
        genresIsUpdating = symphony.groove.genre.isUpdating
        genres.swap(symphony.groove.genre.getAll())
    }

    private fun updatePlaylistsState() {
        playlistsIsUpdating = symphony.groove.playlist.isUpdating
        playlists.swap(symphony.groove.playlist.getAll())
    }
}
