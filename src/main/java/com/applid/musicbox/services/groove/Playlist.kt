package com.applid.musicbox.services.groove

import android.net.Uri
import androidx.compose.runtime.Immutable
import coil.request.ImageRequest
import com.applid.musicbox.Symphony
import com.applid.musicbox.services.parsers.M3U
import com.applid.musicbox.ui.helpers.Assets
import com.applid.musicbox.utils.toList
import org.json.JSONArray
import org.json.JSONObject

@Immutable
data class Playlist(
    val id: String,
    val title: String,
    val songs: List<Long>,
    val numberOfTracks: Int,
    val local: Local?,
) {
    data class LocalExtended(val id: Long, val uri: Uri, val local: Local) {
        val path: String get() = local.path
    }

    data class Local(val path: String) {
        fun toJSONObject(): JSONObject {
            val json = JSONObject()
            json.put(PLAYLIST_LOCAL_PATH_KEY, path)
            return json
        }

        companion object {
            const val PLAYLIST_LOCAL_PATH_KEY = "l_path"

            fun fromJSONObject(serialized: JSONObject): Local {
                return Local(serialized.getString(PLAYLIST_LOCAL_PATH_KEY))
            }
        }
    }

    fun createArtworkImageRequest(symphony: Symphony) =
        songs.firstOrNull()
            ?.let { symphony.groove.song.getSongWithId(it)?.createArtworkImageRequest(symphony) }
            ?: ImageRequest.Builder(symphony.applicationContext)
                .data(Assets.getPlaceholderUri(symphony.applicationContext))

    fun isLocal() = local != null
    fun isNotLocal() = local == null

    fun toJSONObject(): JSONObject {
        val json = JSONObject()
        json.put(PLAYLIST_ID_KEY, id)
        json.put(PLAYLIST_TITLE_KEY, title)
        json.put(PLAYLIST_SONGS_KEY, JSONArray(songs))
        json.put(PLAYLIST_NUMBER_OF_TRACKS_KEY, numberOfTracks)
        return json
    }

    companion object {
        const val PLAYLIST_ID_KEY = "id"
        const val PLAYLIST_TITLE_KEY = "title"
        const val PLAYLIST_SONGS_KEY = "songs"
        const val PLAYLIST_NUMBER_OF_TRACKS_KEY = "n_tracks"

        fun fromJSONObject(serialized: JSONObject): Playlist {
            val songs = serialized.getJSONArray(PLAYLIST_SONGS_KEY)
                .toList { getLong(it) }
            return Playlist(
                id = serialized.getString(PLAYLIST_ID_KEY),
                title = serialized.getString(PLAYLIST_TITLE_KEY),
                songs = songs,
                numberOfTracks = serialized.getInt(PLAYLIST_NUMBER_OF_TRACKS_KEY),
                local = null,
            )
        }

        fun fromM3U(symphony: Symphony, local: LocalExtended): Playlist {
            val path = GrooveExplorer.Path(local.path)
            val dir = path.dirname
            val content = symphony.applicationContext.contentResolver
                .openInputStream(local.uri)
                ?.use { String(it.readBytes()) } ?: ""
            val m3u = M3U.parse(content)
            val songs = mutableListOf<Long>()
            m3u.entries.forEach { entry ->
                val resolvedPath = when {
                    GrooveExplorer.Path.isAbsolute(entry.path) -> entry.path
                    else -> "/" + dir.resolve(GrooveExplorer.Path(entry.path)).toString()
                }
                val id = symphony.groove.song.cachedPaths[resolvedPath]
                id?.let { songs.add(it) }
            }
            return Playlist(
                id = local.path,
                title = path.basename.removeSuffix(".m3u"),
                songs = songs,
                numberOfTracks = songs.size,
                local = local.local,
            )
        }
    }
}
