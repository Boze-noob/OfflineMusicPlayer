package com.applid.musicbox.ads

import android.app.Activity
import com.google.android.gms.ads.interstitial.InterstitialAd

class InterstitialAdHelper {
    fun showAd(mInterstitialAd: InterstitialAd? , activity: Activity) {
        mInterstitialAd?.show(activity)
    }
}