//TODO check this
class SongDownload {   
    suspend fun download(url: String, progressListener: (Int) -> Unit) {
            try {
                if (!isYouTubeUrl(url)) {
                    val audioDownloader = AudioDownloader()

                    audioDownloader.downloadAndTrackProgress(url) { progress ->
                        progressListener(progress)
                        if(progress == 100)  showToast(localContext, audioDownloadedSuccessfullyTxt )
                    }
                } else {
                    val songsApi = SongsApi()
                    songsApi.fetchYouTubeAudioData(localContext, url, {
                            isSuccessful -> GlobalScope.launch(Dispatchers.Main) {
                        if (isSuccessful) {
                            showToast(localContext, audioDownloadedSuccessfullyTxt )
                            // TODO: add song to the list of songs
                        } else showToast(localContext, viewContext.symphony.t.downloadFailedTryAgain, Toast.LENGTH_SHORT)                        
                    }
                    }, {progress -> progressListener(progress)})
                }
            } catch (e: Exception) {
                Log.e("DownloadSongException", e.message ?: "Unknown Error!")
                showToast(localContext, viewContext.symphony.t.unexpectedErrorHappenPleaseTryAgain, Toast.LENGTH_LONG)
        }
    }
}
private fun showToast(context: Context, message: String, length: Toast.length) {
    Toast.makeText(context, "${message}!", Toast.LENGTH_SHORT).show()
}