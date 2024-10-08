package com.vanyscore.notes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanyscore.notes.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NoteScreen(
    noteId: Int?,
) {
    val viewModel = viewModel<NoteViewModel>().apply {
        if (noteId != null) {
            applyNoteId(noteId)
        }
    }
    val state = viewModel.state.collectAsState()
    val note = state.value
    return Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Заметка")
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = padding.calculateBottomPadding()
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            TextField(
                note.title,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Заголовок")
                },
                onValueChange = { value ->
                    viewModel.updateNote(
                        note.copy(
                            title = value
                        )
                    )
                },
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            TextField(
                note.description,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Описание")
                },
                onValueChange = { value ->
                    viewModel.updateNote(
                        note.copy(
                            description = value
                        )
                    )
                }
            )
        }
    }
}