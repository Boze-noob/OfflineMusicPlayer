import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File

class HttpClient {
    private fun create(context: Context, enableCaching: Boolean = false): OkHttpClient {
        val builder = OkHttpClient.Builder()

        if (enableCaching) {
            val cacheDirectory = File(context.cacheDir, "http_cache")

            val cacheSize = 10 * 10 * 1024 * 1024 // 100 MB
            builder.cache(Cache(cacheDirectory, cacheSize.toLong()))
        } else {
            builder.cache(null)
        }

        return builder.build()
    }
}
