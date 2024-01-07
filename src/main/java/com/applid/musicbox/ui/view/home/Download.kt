package com.applid.musicbox.ui.view.home

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.applid.musicbox.ui.components.DownloadSongForm
import com.applid.musicbox.ui.components.LoaderScaffold
import com.applid.musicbox.ui.helpers.ViewContext

@Composable
fun DownloadView(viewContext: ViewContext) {
    val localContext: Context = LocalContext.current

    LoaderScaffold(viewContext, isLoading = false) {
        DownloadSongForm(viewContext = viewContext, localContext = localContext)
    }
}