import android.net.Uri
import java.net.URL

fun isValidUrl(url: String): Boolean {
    val pattern = Regex(
        "^(https?|ftp):\\/\\/" +
                "((([a-zA-Z0-9_\\-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))" +
                "(:[0-9]{1,5})?)" +
                "(\\/\\S*)?$"
    )
    return pattern.matches(url)
}

fun isValidAudioUrl(url: String): Boolean {
    val pattern = Regex(
        "^(https?|ftp):\\/\\/" +
                "((([a-zA-Z0-9_\\-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))" +
                "(:[0-9]{1,5})?)" +
                "(\\/\\S*)?\\.(mp3|wav|aac)$" // Add common audio file extensions here
    )
    return pattern.matches(url)
}
//TODO something not alright with this function
fun getFileNameFromUrl(url: String): String {
    val connection = URL(url).openConnection()
    val contentDisposition = connection.getHeaderField("Content-Disposition")

    if (!contentDisposition.isNullOrBlank()) {
        val fileNameMatcher = Regex("filename\\s*=\\s*\"([^\"]+)\"").find(contentDisposition)
        if (fileNameMatcher != null) {
            return fileNameMatcher.groupValues[1]
        }
    }

    // If content disposition header is not available, extract filename from URL
    val pathSegments = Uri.parse(url).pathSegments
    return pathSegments.lastOrNull() ?: "audio_file" // Default filename if unable to extract from URL
}