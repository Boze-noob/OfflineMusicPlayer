package com.applid.musicbox.ads

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class InterstitialAdHelper {
    fun get(context: Context, adUnitId: String, callback: (InterstitialAd?) -> Unit) {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(context, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                println(adError)
                callback(null)
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                callback(interstitialAd)
            }
        })
    }
}