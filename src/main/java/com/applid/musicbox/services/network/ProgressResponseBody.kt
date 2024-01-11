import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.BufferedSource
import okio.*
class ProgressResponseBody(
    private val responseBody: ResponseBody,
    private val progressListener: (Int) -> Unit
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                if (bytesRead != -1L) {
                    totalBytesRead += bytesRead
                    val progress = ((totalBytesRead * 100) / responseBody.contentLength()).toInt()
                    progressListener(progress)
                }
                return bytesRead
            }
        }
    }
}