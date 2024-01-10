//TODO add imports
fun getFileName(url: String, contentDisposition: String?, mimeType: String?): String {
    var fileName: String = URLUtil.guessFileName(url, contentDisposition, mimeType)

    if (fileName.isEmpty()) fileName = url.substringAfterLast('/')

    return fileName
}