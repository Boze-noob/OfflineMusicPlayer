package com.applid.musicbox.services.groove

data class Genre(
    val name: String,
    // NOTE: mutable cause we handle this
    var numberOfTracks: Int,
)
