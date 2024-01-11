
object Endpoints {
    //TODO add endpoint
    const val YT_AUDIO_DATA_ENDPOINT = ""
    // Add more endpoints as needed
}

//TODO finish this function
suspend fun fetchYtAudioData(context: Context): ByteArray {
    val httpClient = HttpClient.create(context)
    const val url = "$BASE_URL/$Endpoints.YT_AUDIO_DATA_ENDPOINT"

    val request = Request.Builder()
        .url(url)
        .build()

    return httpClient.newCall(request).execute().use { response ->
        if (!response.isSuccessful) {
            throw Exception("Failed to fetch audio data: $response")
        }

        response.body?.bytes() ?: throw Exception("Empty response body")
        //TODO return the result
    }
}