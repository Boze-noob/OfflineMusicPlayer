package com.applid.musicbox.utils

import android.util.Log

object Logger {
    private const val tag = "MusicBoxLogger"

    fun warn(mod: String, text: String) = Log.w(tag, "$mod: $text")
    fun error(mod: String, text: String) = Log.e(tag, "$mod: $text")
}
