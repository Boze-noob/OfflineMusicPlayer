package com.applid.musicbox.ui.view.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.applid.musicbox.ui.components.ScaffoldDialog
import com.applid.musicbox.ui.helpers.ViewContext
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutTile(
    context: ViewContext,
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    dialogContent: String,


) {
    var isOpen by remember { mutableStateOf(false) }

    Card(
        colors = SettingsTileDefaults.cardColors(),
        onClick = {
            isOpen = !isOpen
        }
    ) {
        ListItem(
            colors = SettingsTileDefaults.listItemColors(),
            leadingContent = { icon() },
            headlineContent = { title() },

        )
    }

    if (isOpen) {
        ScaffoldDialog(
            onDismissRequest = {

                    isOpen = false

            },
            title = title,

            content = {
                Column(
                    modifier = Modifier
                        .padding(10.dp, 12.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                   Text(
                       text = dialogContent,
                       textAlign = TextAlign.Center,
                       
                       style = MaterialTheme.typography.bodyMedium
                   )
                    Spacer(modifier = Modifier.height(10.dp))
                    ElevatedButton(
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),

                        onClick = {
                            isOpen = false
                        },
                        modifier = Modifier.fillMaxWidth()

                    ) {
                        Text(context.symphony.t.close)
                    }
                }
            },

        )
    }
}
