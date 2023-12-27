package com.applid.musicbox.ads

import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAdView(adUnitId: String) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        AndroidView(

            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.LARGE_BANNER)
                    setAdUnitId(adUnitId)
                    loadAd(AdRequest.Builder().build())
                }
            },
            update = { adView ->
                adView.loadAd(AdRequest.Builder().build())
            }

        )
    }

}