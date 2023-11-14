import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

class AudioDownloadManager(private val context: Context) {

    fun downloadAudio(url: String) {
       DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
       request.setAllowedNetworkTypes(Request.NETWORK_WIFI);
       request.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
       request.setTitle(context.symphony.t.musicboxDownloadsSong);
       request.setDescription(context.symphony.t.inProgress);
       request.setAllowedOverRoaming(false);
       request.setDestinationUri(Uri.fromFile(new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/musicbox/")));
       long downloadID = downloadManager.enqueue(request);

       return downloadID;
    }
}
