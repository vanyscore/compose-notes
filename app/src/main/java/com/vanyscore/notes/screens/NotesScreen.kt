package com.vanyscore.notes.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanyscore.app.LocalInnerNavController
import com.vanyscore.app.ui.DatePickerBar
import com.vanyscore.app.utils.Logger
import com.vanyscore.notes.domain.Note
import com.vanyscore.notes.ui.NoteItemRedesign
import com.vanyscore.notes.viewmodel.NotesViewModel
import com.vanyscore.tasks.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    sectionId: Int? = null,
    openNote: (Note?) -> Unit,
) {
    Logger.log("NoteScreen: sectionId: $sectionId")
    val viewModel: NotesViewModel = hiltViewModel(
        creationCallback = { factory: NotesViewModel.Factory ->
            factory.create(sectionId)
        }
    )
    val state = viewModel.state.collectAsState().value
    val notes = state.notes
    val navController = LocalInnerNavController.current
    return Scaffold(
        topBar = {
            if (sectionId == null) {
                DatePickerBar(
                    datesSelectedChecker = viewModel
                )
            } else {
                TopAppBar(title = {
                    Text(
                        stringResource(R.string.notes), style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 18.sp
                    ))
                }, colors = TopAppBarDefaults .topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ), navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, "go_back", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },)
            }
        },
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
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (notes.isNotEmpty())
                Notes(notes) {
                    openNote(it)
                }
            else
                NotesEmpty()
        }
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
            NoteItemRedesign(
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
                stringResource(R.string.notes_empty),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}