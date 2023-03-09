package com.applid.musicbox.ui.view.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.applid.musicbox.ui.components.ScaffoldDialog
import com.applid.musicbox.ui.helpers.ViewContext
import com.applid.musicbox.utils.strictEquals
import com.applid.musicbox.utils.swap
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SettingsMultiOptionTile(
    context: ViewContext,
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    note: (@Composable () -> Unit)? = null,
    value: Set<T>,
    values: Map<T, String>,
    satisfies: (Set<T>) -> Boolean = { true },
    onChange: (Set<T>) -> Unit,
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
            headlineText = { title() },
            supportingText = { Text(value.joinToString { values[it]!! }) },
        )
    }

    if (isOpen) {
        val nValue = remember {
            mutableStateListOf<T>().apply { swap(value) }
        }
        val sortedValues by remember {
            derivedStateOf {
                mutableSetOf<T>().apply {
                    addAll(nValue)
                    addAll(values.keys)
                }
            }
        }
        val satisfied by remember {
            derivedStateOf { satisfies(nValue.toSet()) }
        }
        val modified by remember {
            derivedStateOf { !nValue.strictEquals(value.toList()) }
        }

        ScaffoldDialog(
            onDismissRequest = {
                if (!modified) {
                    isOpen = false
                }
            },
            title = title,
            topBar = {
                note?.let {
                    Box(
                        modifier = Modifier
                            .padding(start = 24.dp, end = 24.dp, top = 16.dp)
                            .alpha(0.7f)
                    ) {
                        ProvideTextStyle(MaterialTheme.typography.labelMedium) {
                            it()
                        }
                    }
                }
            },
            content = {
                Column(
                    modifier = Modifier
                        .padding(0.dp, 12.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    sortedValues.mapIndexed { i, key ->
                        val selected = nValue.contains(key)
                        val toggleEntry: () -> Unit = {
                            if (nValue.contains(key)) {
                                nValue.remove(key)
                            } else {
                                nValue.add(key)
                            }
                        }
                        Card(
                            colors = SettingsTileDefaults.cardColors(),
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = toggleEntry,
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp, 0.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = selected,
                                        onCheckedChange = { toggleEntry() },
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(values[key]!!)
                                }
                                if (selected) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(
                                            enabled = i - 1 >= 0,
                                            onClick = {
                                                Collections.swap(nValue, i - 1, i)
                                            }
                                        ) {
                                            Icon(Icons.Default.ArrowUpward, null)
                                        }
                                        IconButton(
                                            enabled = i + 1 < nValue.size,
                                            onClick = {
                                                Collections.swap(nValue, i + 1, i)
                                            }
                                        ) {
                                            Icon(Icons.Default.ArrowDownward, null)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            actions = {
                TextButton(
                    onClick = {
                        isOpen = false
                    }
                ) {
                    Text(context.symphony.t.cancel)
                }
                TextButton(
                    enabled = modified && satisfied,
                    onClick = {
                        onChange(nValue.toSet())
                        isOpen = false
                    }
                ) {
                    Text(context.symphony.t.done)
                }
            },
        )
    }
}
