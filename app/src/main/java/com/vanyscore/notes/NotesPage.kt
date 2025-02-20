package com.vanyscore.notes

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanyscore.notes.domain.Note
import com.vanyscore.notes.viewmodel.NotesViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotesPage(
    viewModel: NotesViewModel = hiltViewModel(),
    openNote: (Note?) -> Unit
) {
    val state = viewModel.state.collectAsState().value
    val notes = state.notes
    return Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    openNote(null)
                }
            ) {
                Icon(
                    Icons.Default.Add,
                    "add_note"
                )
            }
        }
    ) {
        if (notes.isNotEmpty())
            Notes(notes) {
                openNote(it)
            }
        else
            NotesEmpty()
    }
}

@Composable
fun Notes(
    notes: List<Note>,
    onTap: (Note) -> Unit,
) {
    return LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            notes
        ) { note ->
            NoteItem(
                note = note,
                onClick = {
                    onTap(note)
                }
            )
        }
    }
}

@Composable
fun NotesEmpty() {
    return Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Menu,
                "empty_notes",
                modifier = Modifier.size(52.dp),
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Список заметок пуст",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}