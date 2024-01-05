package com.applid.musicbox.services.managers

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.applid.musicbox.services.i18n.Translations
import getFileNameFromUrl



class DownloadManager(context: Context) {

   private val downloadManager = context.getSystemService(DownloadManager::class.java)

    fun downloadAudio(url: String) : Long {
       val request = DownloadManager.Request(Uri.parse(url))
       request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
       request.setTitle(Translations.default.musicboxDownloadsSong)
       request.setDescription(Translations.default.inProgress)
       request.setAllowedOverRoaming(false)

       val fileName = getFileName(url)
       
       request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "musicbox/$fileName")

       return downloadManager.enqueue(request)
    }

   //TODO check if everything is alright
   private fun getFileName(url : String): String {
      var fileName : String = ""
      Bundle extras = intent.getExtras();
      DownloadManager.Query q = new DownloadManager.Query();
      q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
      Cursor c = downloadManager.query(q);

      if (c.moveToFirst()) {
         int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

         val contentDispositionIndex = cursor.getColumnIndex(DownloadManager.COLUMN_CONTENT_DISPOSITION)
         val contentDisposition = cursor.getString(contentDispositionIndex)

         if (status == DownloadManager.STATUS_SUCCESSFUL) {
            //TODO COLUMN_LOCAL_FILENAME could be depricated, use this if it is -> COLUMN_LOCAL_URI
             String filePath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
             filename = filePath.substring( filePath.lastIndexOf('/') + 1, filePath.length() );
         
             if(fileName.isEmpty()) fileName = URLUtil.guessFileName(url, contentDisposition)
         }
      }
      c.close();

      return fileName
      
    }
}
