package com.applid.musicbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applid.musicbox.ui.view.BaseView
import com.applid.musicbox.utils.Logger
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var gSymphony: Symphony? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this) {}

        val ignition: IgnitionViewModel by viewModels()
        if (savedInstanceState == null) {
            installSplashScreen().apply {
                setKeepOnScreenCondition { !ignition.ready.value }
            }
        }

        Thread.setDefaultUncaughtExceptionHandler { _, error ->
            Logger.error("MainActivity", "Uncaught exception: $error")
            error.printStackTrace()
            ErrorActivity.start(this, error)
            finish()
        }

        val symphony: Symphony by viewModels()
        symphony.permission.handle(this)
        gSymphony = symphony
        symphony.ready()
        attachHandlers()

        // NOTE: disables action bar on orientation changes (esp. in miui)
        actionBar?.hide()
        setContent {
            LaunchedEffect(LocalContext.current) {
                if (!ignition.isReady) {
                    ignition.toReady()
                }
            }

            BaseView(symphony = symphony, activity = this)
        }
    }

    override fun onPause() {
        super.onPause()
        gSymphony?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        gSymphony?.destroy()
    }

    private fun attachHandlers() {
        onBackPressedDispatcher.addCallback {
            moveTaskToBack(true)
        }
        gSymphony?.closeApp = {
            finish()
        }
    }
}

class IgnitionViewModel : ViewModel() {
    private val readyFlow = MutableStateFlow(false)
    val ready = readyFlow.asStateFlow()
    val isReady: Boolean
        get() = readyFlow.value

    fun toReady() {
        if (readyFlow.value) return
        viewModelScope.launch { readyFlow.emit(true) }
    }
}
