package com.applid.musicbox.utils

fun Int.shortenNumber(): String {
    return when {
        this < 1000 -> this.toString()
        this < 1000000 -> "${this / 1000}k"
        this < 1000000000 -> "${this / 1000000}M"
        else -> "${this / 1000000000}B"
    }
}
