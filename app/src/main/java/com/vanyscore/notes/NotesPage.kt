package com.vanyscore.notes

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanyscore.notes.domain.Note
import com.vanyscore.notes.viewmodel.NotesViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotesPage(
    viewModel: NotesViewModel = viewModel(),
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
        LazyColumn(
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
                        openNote(note)
                    }
                )
            }
        }
    }
}