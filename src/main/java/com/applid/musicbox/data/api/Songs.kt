import android.content.Context
import com.applid.musicbox.data.BASE_URL
import okhttp3.Request

object Endpoints {
    //TODO add endpoint
    const val YT_AUDIO_DATA_ENDPOINT = ""
    // Add more endpoints as needed
}

//TODO finish this function
suspend fun fetchYtAudioData(context: Context): Boolean {
    val httpClient = HttpClient.create(context)
    val url = "$BASE_URL/$Endpoints.YT_AUDIO_DATA_ENDPOINT"

    val request = Request.Builder()
        .url(url)
        .build()

    httpClient.newCall(request).execute().use { response ->
        if (!response.isSuccessful) {
            throw Exception("Failed to fetch audio data: $response")
        }

        response.body.bytes()
        //TODO return the result
    }
    return false
}