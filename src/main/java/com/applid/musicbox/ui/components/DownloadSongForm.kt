package com.applid.musicbox.ui.components

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.applid.musicbox.ui.helpers.ViewContext
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp
import com.applid.musicbox.ads.BannerAdView
import com.applid.musicbox.ads.DOWNLOAD_SONG_BANNER_AD_UNIT
import com.applid.musicbox.data.api.SongsApi
import com.applid.musicbox.services.downloaders.AudioDownloader
import isValidAudioUrl
import isYouTubeUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@Composable
fun DownloadSongForm (
    viewContext: ViewContext,
    localContext: Context
) {
    var enteredUrl by remember { mutableStateOf(TextFieldValue()) }
    var showUrlValidationError by remember { mutableStateOf(false) }
    var downloadProgress by remember { mutableIntStateOf(0) }

    val coroutineScope = rememberCoroutineScope()

    val audioDownloadedSuccessfullyTxt =  viewContext.symphony.t.audioDownloadedSuccessfully

    fun download(url: String, isYouTubeUrl: Boolean) {
        coroutineScope.launch {
            try {
                if (!isYouTubeUrl) {
                    val audioDownloader = AudioDownloader()

                    audioDownloader.downloadAndTrackProgress(url) { progress ->
                        downloadProgress = progress
                        if(progress == 100)  showToast(localContext, audioDownloadedSuccessfullyTxt, Toast.LENGTH_LONG )
                    }
                } else {
                    val songsApi = SongsApi()
                    songsApi.fetchYouTubeAudioData(localContext, url, {
                            isSuccessful -> GlobalScope.launch(Dispatchers.Main) {
                        if (isSuccessful) {
                            showToast(localContext, audioDownloadedSuccessfullyTxt, Toast.LENGTH_LONG )
                            // TODO: add song to the list of songs
                        } else showToast(localContext, viewContext.symphony.t.downloadFailedTryAgain, Toast.LENGTH_SHORT)                        
                    }
                    }, {progress -> downloadProgress = progress})
                }
            } catch (e: Exception) {
                Log.e("DownloadSongException", e.message ?: "Unknown Error!")
                showToast(localContext, viewContext.symphony.t.unexpectedErrorHappenPleaseTryAgain, Toast.LENGTH_LONG)
            }
        }
    }

     fun handleOnDownloadClick() {
        val url = enteredUrl.text.trim()
        val isYouTubeUrl = isYouTubeUrl(url)

        if (!isValidAudioUrl(url) && !isYouTubeUrl) showUrlValidationError = true
        else {
            if (showUrlValidationError) showUrlValidationError = false
            download(url, isYouTubeUrl)
        }
    }

    MediaSortBarScaffold(
        mediaSortBar = {},
        content = {
            Box(modifier = Modifier.padding(16.dp, 12.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = viewContext.symphony.t.freeSongDownloader,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = viewContext.symphony.t.supportedFormats +  ": MP3, WAV, FLAC",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        modifier= Modifier.fillMaxWidth(),
                        value = enteredUrl,
                        onValueChange = {
                            enteredUrl = it
                            showUrlValidationError = false
                        },
                        label = { Text(viewContext.symphony.t.pasteUrl) },
                        placeholder = { Text("https://example.mp3") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Uri
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = Color.Gray,
                        ),
                        isError = showUrlValidationError,
                        trailingIcon = {
                            if (showUrlValidationError) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = viewContext.symphony.t.warning ,
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        supportingText = {
                            if (showUrlValidationError) {
                                Text(
                                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 0.dp),
                                    text = viewContext.symphony.t.invalidUrl,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        maxLines = 1,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Text(
                            text = "${viewContext.symphony.t.downloadingProgress}: $downloadProgress%",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp),
                            progress = downloadProgress.toFloat() / 100,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    ElevatedButton(
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            handleOnDownloadClick()
                        },
                        enabled = downloadProgress == 0 || downloadProgress == 100
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                fontWeight = FontWeight.Bold,
                                text = viewContext.symphony.t.download,
                                fontSize = 17.sp,
                                modifier = Modifier.padding(vertical = 3.dp, horizontal = 3.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = viewContext.symphony.t.download ,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    BannerAdView(adUnitId = DOWNLOAD_SONG_BANNER_AD_UNIT)
                }
            }
        }
    )
}
private fun showToast(context: Context, message: String, length: Int) {
    Toast.makeText(context, "${message}!", length).show()
}
