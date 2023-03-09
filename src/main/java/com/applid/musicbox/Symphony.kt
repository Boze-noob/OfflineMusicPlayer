package com.applid.musicbox

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.applid.musicbox.services.PermissionsManager
import com.applid.musicbox.services.SettingsManager
import com.applid.musicbox.services.database.Database
import com.applid.musicbox.services.groove.GrooveManager
import com.applid.musicbox.services.i18n.Translations
import com.applid.musicbox.services.i18n.Translator
import com.applid.musicbox.services.radio.Radio
import com.applid.musicbox.utils.AndroidXShorty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface SymphonyHooks {
    fun onSymphonyReady() {}
    fun onSymphonyPause() {}
    fun onSymphonyDestroy() {}
}

class Symphony(application: Application) : AndroidViewModel(application), SymphonyHooks {
    val shorty = AndroidXShorty(this)
    val permission = PermissionsManager(this)
    val settings = SettingsManager(this)
    val database = Database(this)
    val groove = GrooveManager(this)
    val radio = Radio(this)

    val translator = Translator(this)
    val t: Translations
        get() = translator.t

    val applicationContext: Context
        get() = getApplication<Application>().applicationContext
    var closeApp: (() -> Unit)? = null
    private var isReady = false
    private var hooks = listOf(this, radio, groove)

    fun ready() {
        if (isReady) return
        isReady = true
        notifyHooks { onSymphonyReady() }
    }

    fun pause() {
        notifyHooks { onSymphonyPause() }
    }

    fun destroy() {
        notifyHooks { onSymphonyDestroy() }
    }

    override fun onSymphonyReady() {}

    private fun notifyHooks(fn: SymphonyHooks.() -> Unit) {
        hooks.forEach { fn.invoke(it) }
    }

}
