package com.vanyscore.notes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanyscore.app.AppState
import com.vanyscore.notes.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    noteId: Int?,
) {
    val appState = AppState.source.collectAsState()
    val navController = appState.value.navController
    val isViewModelInit = remember {
        mutableStateOf(false)
    }
    val viewModel = viewModel<NoteViewModel>().apply {
        if (!isViewModelInit.value && noteId != null) {
            applyNoteId(noteId)
            isViewModelInit.value = true
        }
    }
    val state = viewModel.state.collectAsState()
    val note = state.value.note
    if (state.value.canClose) {
        navController?.navigateUp()
    }
    return Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Заметка")
                },
                navigationIcon = {
                    if (navController?.previousBackStackEntry != null) {
                        IconButton(onClick = {
                            navController.navigateUp()
                        }) {
                            Icon(Icons.Default.ArrowBack, "back")
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White
            ) {
                Button(
                    modifier = Modifier
                        .height(52.dp)
                        .fillMaxWidth(),
                    onClick = {
                        val appDate = appState.value.date
                        viewModel.saveNote(appDate)
                    },
                    enabled = note.title.isNotEmpty() && note.description.isNotEmpty()
                ) {
                    Text("Сохранить")
                }
            }
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