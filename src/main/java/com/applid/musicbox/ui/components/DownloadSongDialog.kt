package com.applid.musicbox.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import com.applid.musicbox.ui.helpers.ViewContext
import isValidUrl
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadSongDialog(
    context: ViewContext,
    onDismissRequest: (isDownloadRequested: Boolean, url: String) -> Unit,
) {
        var enteredUrl by remember { mutableStateOf("") }
        var showUrlValidationError by remember { mutableStateOf(false) }

    fun handleOnDownloadClick(enteredUrl: String) {
        if (isValidUrl(enteredUrl)) onDismissRequest(true, enteredUrl)
        else showUrlValidationError = true
    }

    ScaffoldDialog(
        onDismissRequest = { onDismissRequest(false, "") },
        title = {
            Text(
                context.symphony.t.downloadSong
            )
        },
        content = {
            Box(modifier = Modifier.padding(16.dp, 12.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        textAlign = TextAlign.Center,

                        text = context.symphony.t.downloadSong
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = enteredUrl,
                        onValueChange = { newText : String ->
                            enteredUrl = newText
                        },
                        label = { Text(context.symphony.t.pasteUrl) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = Color.Gray,
                        ),
                        isError = showUrlValidationError,
                        supportingText = {
                            if (showUrlValidationError) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = context.symphony.t.invalidUrl,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ElevatedButton(
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            handleOnDownloadClick(enteredUrl)
                        },

                        ) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = context.symphony.t.download
                        )
                    }
                    Button(
                        onClick = {
                            onDismissRequest(false, "")
                        },
                        colors = ButtonDefaults.buttonColors(
                            Color.Transparent,
                            contentColor = Color.Gray
                        )
                    ) {
                        Text(
                            style = MaterialTheme.typography.titleSmall,
                            text = context.symphony.t.cancel
                        )
                    }
                }
            }
        }
    )
}
