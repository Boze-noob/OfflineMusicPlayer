package com.applid.musicbox.utils

fun Float.toSafeFinite() = if (!isFinite()) 0f else this