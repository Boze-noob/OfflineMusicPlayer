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
                "(\\/\\S*)?\\.(mp3|wav|aac)$" 
    )
    return pattern.matches(url)
}

fun isYoutubeUrl(url: String): Boolean {
    return url.contains("youtube.com") || url.contains("youtu.be")
}