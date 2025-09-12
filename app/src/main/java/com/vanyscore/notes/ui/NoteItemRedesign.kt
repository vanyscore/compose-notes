package com.vanyscore.notes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanyscore.notes.domain.Note
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NoteItemRedesign(
    note: Note,
    onClick: () -> Unit,
) {
    val title = note.title
    val desc = note.description
    val dt = note.edited
    val color = title.toColor()
    val formatter = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }
    return Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max)
            .heightIn(max = 300.dp)
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                onClick()
            }
    ) {
        Row {
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .fillMaxHeight()
                    .background(color = color)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ))
                Spacer(modifier = Modifier.height(8.dp))
                Text(desc, style = TextStyle(
                    fontSize = 14.sp,
                ), overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Последнее изменение: ${formatter.format(dt)}", style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                ))
            }
        }
    }
}