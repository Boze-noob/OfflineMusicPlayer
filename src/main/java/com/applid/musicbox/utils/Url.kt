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