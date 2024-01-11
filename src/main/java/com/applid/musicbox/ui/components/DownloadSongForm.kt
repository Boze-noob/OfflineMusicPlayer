package com.applid.musicbox.ui.components

import android.content.Context
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
import com.applid.musicbox.services.downloaders.AudioDownloader
import isValidAudioUrl
import isYoutubeUrl
import kotlinx.coroutines.launch
import fetchYtAudioData

@Composable
fun DownloadSongForm (
    viewContext: ViewContext,
    localContext: Context
) {
    var enteredUrl by remember { mutableStateOf(TextFieldValue()) }
    var showUrlValidationError by remember { mutableStateOf(false) }
    var downloadProgress by remember { mutableIntStateOf(0) }

    val coroutineScope = rememberCoroutineScope()

    fun handleOnDownloadClick(enteredUrl: String) {
        if (!isValidAudioUrl(enteredUrl)) showUrlValidationError = true
        else {
            if (showUrlValidationError) showUrlValidationError = false

            coroutineScope.launch {
                try {
                    if(isYoutubeUrl(enteredUrl)) {

                    val audioDownloader = AudioDownloader()

                    audioDownloader.downloadAndTrackProgress(enteredUrl, localContext) { progress ->
                        downloadProgress = progress
                    }
                    } else {
                        val response : Boolean = fetchYtAudioData(localContext)
                        //TODO decide what to do next, if successful show some message and include new song to the songs list, if not show some message
                    }
                } catch (e: Exception) {
                    Toast.makeText(localContext, viewContext.symphony.t.unexpectedErrorHappenPleaseTryAgain, Toast.LENGTH_LONG).show()
                }
            }
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
                            showUrlValidationError = false // Reset error state on input change
                        },
                        label = { Text(viewContext.symphony.t.pasteUrl) },
                        placeholder = { Text("https://example.com") },
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
                            text = "Downloading Progress: $downloadProgress%",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth().height(6.dp),
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
                            handleOnDownloadClick(enteredUrl.text)
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
                }
            }
        }
    )
}
