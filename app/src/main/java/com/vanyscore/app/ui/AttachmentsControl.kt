package com.vanyscore.app.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.vanyscore.tasks.R
import kotlin.math.ceil

enum class ControlType {
    ROW, GRID
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AttachmentsControl(
    controlType: ControlType = ControlType.ROW,
    attachments: List<Uri> = emptyList(),
    onAttachmentAdd: ((uri: Uri) -> Unit)? = null,
    onAttachmentRemove: ((uri: Uri) -> Unit)? = null,
) {
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        val uri = it
        if (uri != null) {
            onAttachmentAdd?.invoke(uri)
        }
    }
    val selectedAttachments = remember {
        mutableStateListOf<Uri>()
    }
    return Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        val cellSize = LocalConfiguration.current.screenWidthDp / 3
        val rows = ceil(attachments.size / 3.0)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.attachments)
            )
            Row {
                if (selectedAttachments.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            selectedAttachments.forEach {
                                onAttachmentRemove?.invoke(it)
                            }
                            selectedAttachments.clear()
                        }
                    ) {
                        Icon(
                            Icons.Default.Delete, "add_attachment"
                        )
                    }
                }
                IconButton(
                    onClick = {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                ) {
                    Icon(
                        Icons.Default.Add, "add_attachment"
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(
                (rows * cellSize).dp
            )
        ) {
            items(
                count = attachments.size
            ) { index ->
                val attachment = attachments[index]
                AsyncImage(
                    attachment, "async_image",
                    modifier = Modifier
                        .size(cellSize.dp)
                        .border(2.dp,
                            if (selectedAttachments.singleOrNull {
                                    it === attachment
                                } != null
                            ) MaterialTheme.colorScheme.primary
                            else Color.White
                        )
                        .combinedClickable(
                            onClick = {
                                if (selectedAttachments.isNotEmpty()) {
                                    val foundIndex = selectedAttachments.indexOfFirst {
                                        it === attachment
                                    }
                                    if (foundIndex != -1) {
                                        selectedAttachments.removeAt(foundIndex)
                                    } else {
                                        selectedAttachments.add(attachment)
                                    }
                                }
                            },
                            onLongClick = {
                                if (selectedAttachments.singleOrNull {
                                    it === attachment
                                } == null) {
                                    selectedAttachments.add(attachment)
                                }
                            }
                        ),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}