package com.applid.musicbox.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.applid.musicbox.ui.components.IconTextBody
import com.applid.musicbox.ui.helpers.ViewContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NothingPlaying(context: ViewContext) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NowPlayingAppBar(context)
        },
        content = { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                NothingPlayingBody(context)
            }
        }
    )
}

@Composable
fun NothingPlayingBody(context: ViewContext) {
    IconTextBody(
        icon = { modifier ->
            Icon(
                Icons.Default.Headphones,
                null,
                modifier = modifier
            )
        },
        content = {
            Text(context.symphony.t.nothingIsBeingPlayedRightNow)
        }
    )
}
