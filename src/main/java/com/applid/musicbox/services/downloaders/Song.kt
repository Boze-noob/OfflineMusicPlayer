package com.applid.musicbox.services.downloaders

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.work.*
import com.applid.musicbox.ui.helpers.ViewContext
import org.yausername.dvd.R
import org.yausername.dvd.work.CommandWorker
import org.yausername.dvd.work.CommandWorker.Companion.commandKey

class SongDownloader(private val localContext: Context, private val viewContext: ViewContext) {

fun download(url : String) {

    if (isStoragePermissionGranted() && url.isNotBlank()) {
        val command: String = "--extract-audio --audio-format mp3 -o /sdcard/Download/%(title)s.%(ext)s $url";
        startCommand(command)
    } 
}

    private fun startCommand(command: String) {
        val workTag = CommandWorker.commandWorkTag
        //TODO pass context
        val workManager = WorkManager.getInstance(activity?.applicationContext!!)
        val state = workManager.getWorkInfosByTag(workTag).get()?.getOrNull(0)?.state
        val running = state === WorkInfo.State.RUNNING || state === WorkInfo.State.ENQUEUED
        if (running) {
            Toast.makeText(
                localContext,
                viewContext.symphony.t.commandAlreadyRunning,
                Toast.LENGTH_LONG
            ).show()
            return
        }
        val workData = workDataOf(
            commandKey to command
        )
        val workRequest = OneTimeWorkRequestBuilder<CommandWorker>()
            .addTag(workTag)
            .setInputData(workData)
            .build()

        workManager.enqueueUniqueWork(
            workTag,
            ExistingWorkPolicy.KEEP,
            workRequest
        )
        Toast.makeText(
            localContext,
            viewContext.symphony.t.commandQueued,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    //TODO pass context
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else { 
            //permission is automatically granted on sdk<23 upon installation
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCommand(command!!)
        }
    }
}