package com.applid.musicbox.services.groove

import com.applid.musicbox.Symphony
import com.applid.musicbox.utils.Eventer
import com.applid.musicbox.utils.FuzzySearchOption
import com.applid.musicbox.utils.FuzzySearcher
import com.applid.musicbox.utils.subListNonStrict

enum class GenreSortBy {
    CUSTOM,
    GENRE,
    TRACKS_COUNT,
}

class GenreRepository(private val symphony: Symphony) {
    var isUpdating = false
    val onUpdate = Eventer<Nothing?>()

    private val searcher = FuzzySearcher<Genre>(
        options = listOf(FuzzySearchOption({ it.name }))
    )

    fun getAll() = symphony.groove.song.cachedGenres.values.toList()

    fun search(terms: String) = searcher.search(terms, getAll()).subListNonStrict(7)

    companion object {
        fun sort(genres: List<Genre>, by: GenreSortBy, reversed: Boolean): List<Genre> {
            val sorted = when (by) {
                GenreSortBy.CUSTOM -> genres.toList()
                GenreSortBy.GENRE -> genres.sortedBy { it.name }
                GenreSortBy.TRACKS_COUNT -> genres.sortedBy { it.numberOfTracks }
            }
            return if (reversed) sorted.reversed() else sorted
        }
    }
}
