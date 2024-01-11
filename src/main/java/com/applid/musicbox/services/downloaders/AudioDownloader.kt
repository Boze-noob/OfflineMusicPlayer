package com.applid.musicbox.services.downloaders

import ProgressResponseBody
import android.content.Context
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import okio.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import getFileName

class AudioDownloader {

    suspend fun downloadAndTrackProgress(url: String, progressListener: (Int) -> Unit) {
        withContext(Dispatchers.IO) {

            val client = OkHttpClient.Builder()
                .addNetworkInterceptor { chain ->
                    val originalResponse = chain.proceed(chain.request())
                    originalResponse.newBuilder()
                        .body(ProgressResponseBody(originalResponse.body, progressListener))
                        .build()
                }
                .build()

            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw RuntimeException("Failed to download file: $response")
                }

                // Get the response body
                val responseBody: ResponseBody = response.body
                val contentDisposition = response.header("Content-Disposition")
                val contentType = response.header("Content-Type")

                val fileName: String = getFileName(url, contentDisposition, contentType)
                // Open the output file

                val outputFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path, fileName)

                val outputStream : OutputStream = FileOutputStream(outputFile)

                // Write the response body to the output file
                responseBody.byteStream().use { inputStream ->
                    val buffer = ByteArray(4 * 1024) // Adjust the buffer size as needed
                    var bytesRead: Int
                    var totalBytesRead: Long = 0
                    val contentLength = responseBody.contentLength()

                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead
                        val progress = ((totalBytesRead * 100) / contentLength).toInt()
                        progressListener(progress)
                    }
                    outputStream.flush()
                }

                outputStream.close()
            }
        }
    }
}
