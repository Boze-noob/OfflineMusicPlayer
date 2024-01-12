import android.content.Context
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object Endpoints {
    const val DOWNLOAD_YT_AUDIO_ENDPOINT = "download_yt_audio"
    // Add more endpoints as needed
}

class SongsApi {
//TODO check if works properly
    suspend fun fetchYtAudioData(context: Context, youtubeUrl: String, callback: (Boolean) -> Unit) {
        val httpClient = HttpClient.create(context)
        val url = "$BASE_URL/${Endpoints.DOWNLOAD_YT_AUDIO_ENDPOINT}"

        val requestBody = FormBody.Builder()
            .add("url", youtubeUrl)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("IOException", e.toMessage())
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val outputStream: FileOutputStream
                    val file = File(context.cacheDir, "audio.mp3")

                    try {
                        outputStream = FileOutputStream(file)
                        response.body?.byteStream()?.use { input ->
                            val buffer = ByteArray(4 * 1024)
                            var read: Int
                            while (input.read(buffer).also { read = it } != -1) {
                                outputStream.write(buffer, 0, read)
                            }
                            outputStream.flush()
                        }
                        callback(true)
                    } catch (e: IOException) {
                        callback(false)
                        Log.e("IOException", e.toMessage())
                    } finally {
                        try {
                            outputStream.close()
                        } catch (e: IOException) {
                            Log.e("IOException", e.toMessage())
                        }
                    }
                } else {
                    callback(false)
                }
            }
        })
    }
}
