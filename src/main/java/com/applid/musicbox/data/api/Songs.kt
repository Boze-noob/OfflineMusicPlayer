import android.content.Context
import android.os.Environment
import android.util.Log
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import com.applid.musicbox.data.BASE_URL
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

object Endpoints {
    const val DOWNLOAD_YT_AUDIO_ENDPOINT = "download_yt_audio"
    // Add more endpoints as needed
}

class SongsApi {
    fun fetchYtAudioData(context: Context, youtubeUrl: String, successCallback: (Boolean) -> Unit,  progressCallback: (Int) -> Unit) {
        val httpClient = HttpClient.create(context)
        val url = "$BASE_URL/${Endpoints.DOWNLOAD_YT_AUDIO_ENDPOINT}"

    val json = """
    {
        "url": "$youtubeUrl"
    }
    """.trimIndent()

    val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("IOException", e.message ?: "IOException")
                successCallback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val contentDispositionHeader = response.header("Content-Disposition")
                    val totalFileSize = response.header("Content-Length")?.toLong() ?: -1
                    val title = extractTitleFromContentDisposition(contentDispositionHeader)

                    var outputStream: FileOutputStream? = null
                    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path,
                        title
                    )

                    try {
                        outputStream = FileOutputStream(file)

                        response.body.byteStream().use { input ->
                            val buffer = ByteArray(4 * 1024)
                            var read: Int
                            var fileSizeDownloaded: Long = 0

                            while (input.read(buffer).also { read = it } != -1) {
                                outputStream.write(buffer, 0, read)

                                fileSizeDownloaded += read.toLong()

                                progressCallback(((fileSizeDownloaded * 100) / totalFileSize).toInt())
                            }
                            outputStream.flush()
                        }
                        successCallback(true)
                    } catch (e: IOException) {
                        successCallback(false)
                        Log.e("IOException", e.message ?: "IOException")
                    } finally {
                        try {
                            outputStream?.close()
                        } catch (e: IOException) {
                            Log.e("IOException", e.message ?: "IOException")
                        }
                    }
                } else {
                    Log.e("Response Error", response.code.toString())
                    Log.e("Response Body", response.body.string())
                    successCallback(false)
                }
            }
        })
    }
}

private fun extractTitleFromContentDisposition(contentDisposition: String?): String {
    val regex = Regex("filename=\"(.*?)\"")
    val matchResult = regex.find(contentDisposition ?: "") ?: return "audio"
    return matchResult.groupValues[1]
}
