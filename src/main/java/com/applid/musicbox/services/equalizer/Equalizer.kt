package com.applid.musicbox.services.equalizer

import android.media.audiofx.Equalizer
import com.applid.musicbox.utils.shortenNumber

class Equalizer {
    private var equalizer: Equalizer? = null
    private var bandLevels: MutableMap<Int, Short> = mutableMapOf()


    fun initialize (audioSessionId : Int?) : Boolean {
        if(audioSessionId == null)
            return false

        equalizer = Equalizer(0, audioSessionId)
        equalizer?.enabled = true

        // If band levels are already set, apply them to the new session
        if (bandLevels.isNotEmpty()) {
            val numberOfBands = getNumberOfBands()?.toInt() ?: 0
            for (i in 0 until numberOfBands) {
                val bandLevel = bandLevels[i]
                if(bandLevel != null) equalizer?.setBandLevel(i.toShort(), bandLevel)
            }
        }
        return true
    }

    fun release () {
        val isEnabled = isEnabled()
        if(isEnabled != null && isEnabled == true) {
            try {
                equalizer?.enabled = false
                equalizer?.release()
                equalizer = null
            } catch (e: java.lang.Exception) {
                println(e)
            }
        }
    }

    fun getMinBandLevel(): Short? = equalizer?.bandLevelRange?.get(0)

    fun getMaxBandLevel() : Short? = equalizer?.bandLevelRange?.get(1)

    fun getNumberOfBands(): Short? = equalizer?.numberOfBands

    fun setBandLevel(band: Short, level: Short) {
        equalizer?.setBandLevel(band, level)
        bandLevels[band.toInt()] = level
    }
    fun getBandLevel(band: Short) = equalizer?.getBandLevel(band)

    fun getCenterFreq(band: Short): String? = equalizer?.getCenterFreq(band)?.div(1000)?.shortenNumber()

    fun isEnabled() = equalizer?.enabled
}