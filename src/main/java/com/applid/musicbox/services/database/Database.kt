package com.applid.musicbox.services.database

import com.applid.musicbox.Symphony

class Database(symphony: Symphony) {
    val songCache = SongCache(symphony)
    val playlists = PlaylistsBox(symphony)
}
