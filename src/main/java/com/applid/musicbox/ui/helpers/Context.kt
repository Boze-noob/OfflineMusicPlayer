package com.applid.musicbox.ui.helpers

import androidx.navigation.NavHostController
import com.applid.musicbox.MainActivity
import com.applid.musicbox.Symphony

data class ViewContext(
    val symphony: Symphony,
    val activity: MainActivity,
    val navController: NavHostController
)
