package com.applid.musicbox.ads

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import com.google.android.gms.ads.AdLoader
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.view.isVisible
import com.applid.musicbox.databinding.NativeAdViewBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAdOptions

@Composable
fun NativeAdView(adUnitId: String) {
    Box {
        AndroidViewBinding(
            factory = { inflater, parent, attachToParent ->
                val binding = NativeAdViewBinding.inflate(inflater, parent, attachToParent)
                val adView = binding.root.also { adView ->
                    adView.headlineView = binding.adHeadline
                    adView.iconView = binding.adAppIcon
                }
                try {
                    val adLoader = AdLoader.Builder(
                        adView.context,
                        adUnitId,
                    )
                        .forNativeAd { nativeAd ->
                            nativeAd.icon?.let {
                                binding.adAppIcon.setImageDrawable(it.drawable)
                                binding.adAppIcon.isVisible = true
                            }
                            nativeAd.headline?.let {
                                if (it.isNotBlank()) {
                                    binding.adHeadline.text = it
                                    binding.adHeadline.isVisible = true
                                }
                            }
                            adView.setNativeAd(nativeAd)
                        }
                        .withAdListener(
                            object : AdListener() {
                                override fun onAdFailedToLoad(error: LoadAdError) {
                                    Log.e("NativeAdViewError", error.message)
                                    super.onAdFailedToLoad(error)
                                }
                            },
                        )
                        .withNativeAdOptions(NativeAdOptions.Builder().build())
                        .build()

                    adLoader.loadAd(AdRequest.Builder().build())
                } catch (e: Exception) {
                    Log.e("Exception", e.message.toString())
                }
                binding
            }
        )
    }
}