package com.applid.musicbox.services.groove

import com.applid.musicbox.Symphony
import com.applid.musicbox.utils.Eventer
import com.applid.musicbox.utils.subListNonStrict

class AlbumArtistRepository(private val symphony: Symphony) {
    var isUpdating = false
    val onUpdate = Eventer<Nothing?>()

    fun getAll() = symphony.groove.song.getAlbumArtistNames().mapNotNull { artist ->
        symphony.groove.artist.getArtistFromName(artist)
    }

    fun search(terms: String) =
        symphony.groove.artist.searcher.search(terms, getAll()).subListNonStrict(7)
}