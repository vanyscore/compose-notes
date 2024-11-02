package com.vanyscore.app.ui

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.vanyscore.tasks.R

enum class ControlType {
    ROW, GRID
}

@Composable
fun AttachmentsControl(
    controlType: ControlType = ControlType.ROW,
    attachments: List<Uri> = emptyList(),
    onAttachmentAdd: ((uri: Uri) -> Unit)? = null,
    onAttachmentRemove: ((uri: Uri) -> Unit)? = null,
) {
    return Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.attachments)
            )
            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    Icons.Default.Add, "add_attachment"
                )
            }
        }
        Spacer(
            modifier = Modifier.height(16.dp)
        )
//        LazyVerticalGrid(
//            columns = GridCells.Adaptive(120.dp),
//        ) {
//            items(
//                count = attachments.size
//            ) { index ->
//
//            }
//        }
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                count = attachments.size
            ) { index ->
                AsyncImage(
                    attachments[index],
                    "picture",
                )
            }
        }
    }
}