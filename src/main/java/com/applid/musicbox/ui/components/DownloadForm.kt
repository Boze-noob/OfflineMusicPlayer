package com.applid.musicbox.ui.components

import android.content.Context
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
import com.applid.musicbox.services.managers.DownloaderManager
import isValidUrl
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp

@Composable
fun DownloadForm (
    viewContext: ViewContext,
    localContext: Context
) {
    var enteredUrl by remember { mutableStateOf(TextFieldValue()) }
    var showUrlValidationError by remember { mutableStateOf(false) }

    fun handleOnDownloadClick(enteredUrl: String, context: Context) {
        if (!isValidUrl(enteredUrl)) showUrlValidationError = true
        else {
            if(showUrlValidationError) showUrlValidationError = false
            val downloadManager = DownloaderManager(context)
            downloadManager.downloadAudio(enteredUrl)

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
                    ElevatedButton(
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            handleOnDownloadClick(enteredUrl.text, localContext)
                        },
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