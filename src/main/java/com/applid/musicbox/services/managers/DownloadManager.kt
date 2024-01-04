package com.applid.musicbox.services.managers

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.applid.musicbox.services.i18n.Translations

class DownloadManager(private val context: Context) {

   private val downloadManager = context.getSystemService(DownloadManager::class.java)

    fun downloadAudio(url: String) : Long {
       val request = DownloadManager.Request(Uri.parse(url))
       request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
       request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
       request.setTitle(Translations.default.musicboxDownloadsSong)
       request.setDescription(Translations.default.inProgress)
       request.setAllowedOverRoaming(false)
       request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "musicbox")

       return downloadManager.enqueue(request)
    }
}
