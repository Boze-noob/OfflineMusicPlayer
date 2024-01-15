import android.content.Context
import android.util.Log
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import com.applid.musicbox.data.BASE_URL

object Endpoints {
    const val DOWNLOAD_YT_AUDIO_ENDPOINT = "download_yt_audio"
    // Add more endpoints as needed
}

class SongsApi {
//TODO check if works properly
    fun fetchYtAudioData(context: Context, youtubeUrl: String, callback: (Boolean) -> Unit) {
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
                Log.e("IOException", e.message ?: "IOException")
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    var outputStream: FileOutputStream? = null
                    val file = File(context.cacheDir, "audio.mp3")

                    try {
                        outputStream = FileOutputStream(file)
                        response.body.byteStream().use { input ->
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
                        Log.e("IOException", e.message ?: "IOException")
                    } finally {
                        try {
                            outputStream?.close()
                        } catch (e: IOException) {
                            Log.e("IOException", e.message ?: "IOException")
                        }
                    }
                } else {
                    callback(false)
                }
            }
        })
    }
}
