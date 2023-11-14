package com.applid.musicbox.services.i18n

import com.applid.musicbox.services.i18n.translations.English

interface Translations {
    val rateUsTitle: String
    val rateUsContent: String
    val notNow: String
    val rateUsOnGooglePlay: String
    val info: String
    val close: String
    val aboutUsContent : String
    val otherApps : String
    val contactUs : String
    val downloadSong: String
    val inProgress : String
    val musicboxDownloadsSong : String
    val download : String
    val pasteUrl : String
    val invalidUrl : String
    val language: String
    val unk: String
    val songs: String
    val artists: String
    val albums: String
    val settings: String
    val details: String
    val path: String
    val filename: String
    val size: String
    val dateAdded: String
    val lastModified: String
    val length: String
    val bitrate: String
    val trackName: String
    val artist: String
    val album: String
    val albumArtist: String
    val composer: String
    val nothingIsBeingPlayedRightNow: String
    val addToQueue: String
    val queue: String
    val playNext: String
    val nowPlaying: String
    val language_: String
    val materialYou: String
    val system: String
    val light: String
    val dark: String
    val black: String
    val viewArtist: String
    val title: String
    val duration: String
    val year: String
    val viewAlbum: String
    val searchYourMusic: String
    val noResultsFound: String
    val albumCount: String
    val trackCount: String
    val filteringResults: String
    val appearance: String
    val about: String
    val github: String
    val play: String
    val previous: String
    val next: String
    val pause: String
    val done: String
    val groove: String
    val songsFilterPattern: String
    val reset: String
    val theme: String
    val checkForUpdates: String
    val version: String
    val shufflePlay: String
    val viewAlbumArtist: String
    val stop: String
    val all: String
    val fadePlaybackInOut: String
    val requireAudioFocus: String
    val ignoreAudioFocusLoss: String
    val player: String
    val playOnHeadphonesConnect: String
    val pauseOnHeadphonesDisconnect: String
    val genre: String
    val damnThisIsSoEmpty: String
    val primaryColor: String
    val playAll: String
    val forYou: String
    val suggestedAlbums: String
    val suggestedArtists: String
    val recentlyAddedSongs: String
    val sponsorViaGitHub: String
    val clearSongCache: String
    val songCacheCleared: String
    val albumArtists: String
    val genres: String
    val cancel: String
    val homeTabs: String
    val selectAtleast2orAtmost5Tabs: String
    val folders: String
    val invisible: String
    val alwaysVisible: String
    val visibleWhenActive: String
    val bottomBarLabelVisibility: String
    val playlists: String
    val newPlaylist: String
    val importPlaylist: String
    val noInAppPlaylistsFound: String
    val noLocalPlaylistsFound: String
    val custom: String
    val playlist: String
    val addSongs: String
    val addToPlaylist: String
    val isLocalPlaylist: String
    val yes: String
    val no: String
    val manageSongs: String
    val delete: String
    val deletePlaylist: String
    val areYouSureThatYouWantToDeleteThisPlaylist: String
    val trackNumber: String
    val tree: String
    val loading: String
    val name: String
    val addFolder: String
    val blacklistFolders: String
    val whitelistFolders: String
    val pickFolder: String
    val invalidM3UFile: String
    val discord: String
    val reddit: String
    val reportAnIssue: String
    val noFoldersFound: String
    val sleepTimer: String
    val hours: String
    val minutes: String
    val quitAppOnEnd: String
    val favorite: String
    val unfavorite: String
    val bitDepth: String
    val samplingRate: String
    val showAudioInformation: String
    val fastRewindDuration: String
    val fastForwardDuration: String
    val enableSeekControls: String
    val miniPlayerTrackControls: String
    val miniPlayerSeekControls: String

    fun XSongs(x: Int): String
    fun playingXofY(x: Int, y: Int): String
    fun unknownArtistX(name: String): String
    fun unknownAlbumX(id: Long): String
    fun XArtists(x: Int): String
    fun XAlbums(x: Int): String
    fun madeByX(x: String): String
    fun newVersionAvailableX(x: String): String
    fun XKbps(x: Int): String
    fun XSecs(x: Float): String
    fun unknownGenreX(x: String): String
    fun XGenres(x: Int): String
    fun XFoldersYFiles(x: Int, y: Int): String
    fun XItems(x: Int): String
    fun XPlaylists(x: Int): String
    fun unknownPlaylistX(x: String): String
    fun XFolders(x: Int): String
    fun XBit(x: Int): String
    fun XKHz(x: Float): String

    companion object {
        val all = arrayOf<Translations>(English())
        val default = all.first()

        fun of(language: String) = all.find {
            it.language == language
        }
    }
}

object CommonTranslations {
    val somethingWentHorriblyWrong = "Something went horribly wrong!"

    fun errorX(x: String) = "Error: $x"
    fun somethingWentHorriblyWrongErrorX(x: String) =
        "$somethingWentHorriblyWrong (${errorX(x)})"
}
