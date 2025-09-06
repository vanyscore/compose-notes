package com.vanyscore.notes.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanyscore.app.composes.BackButton
import com.vanyscore.app.navigation.LocalMainNavController
import com.vanyscore.app.ui.AttachmentsControl
import com.vanyscore.app.ui.noIndicationClickable
import com.vanyscore.app.viewmodel.LocalAppViewModel
import com.vanyscore.notes.viewmodel.NoteViewModel
import com.vanyscore.tasks.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    noteId: Int?,
) {
    val appViewModel = LocalAppViewModel.current
    val appState = appViewModel.state.collectAsState()
    val navController = LocalMainNavController.current
    val isViewModelInit = remember {
        mutableStateOf(false)
    }

    val viewModel = hiltViewModel<NoteViewModel>().apply {
        if (!isViewModelInit.value && noteId != null) {
            attachNoteId(noteId)
            isViewModelInit.value = true
        }
    }
    val state = viewModel.state.collectAsState()
    val note = state.value.note
    if (state.value.canClose) {
        navController.navigateUp()
    }
    val focusManager = LocalFocusManager.current
    return Scaffold(
        modifier = Modifier.noIndicationClickable {
            focusManager.clearFocus()
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.note), style = TextStyle(
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    ))
                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        BackButton {
                            navController.navigateUp()
                        }
                    }
                },
                actions = {
                    if (note.id != null) {
                        RemoveNoteButton()
                    }
                    EditModeButton()
                }
            )
        },
        bottomBar = {
            if (!state.value.isEdit) return@Scaffold
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
                    Text(stringResource(R.string.save))
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = padding.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = padding.calculateBottomPadding()
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
            }
            item {
                TextField(
                    note.title,
                    singleLine = true,
                    enabled = state.value.isEdit,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    label = {
                        Text(stringResource(R.string.title))
                    },
                    onValueChange = { value ->
                        viewModel.updateNote(
                            note.copy(
                                title = value
                            )
                        )
                    },
                )
            }
            item {
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }
            item {
                TextField(
                    note.description,
                    enabled = state.value.isEdit,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(stringResource(R.string.description))
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
            item {
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }
            item {
                AttachmentsControl(
                    attachments = note.images.map {
                        it.uri
                    },
                    onAttachmentAdd = { uri ->
                        viewModel.attachAttachment(
                            uri = uri,
                            note = note,
                        )
                    },
                    isEnabled = state.value.isEdit,
                    onAttachmentRemove = { uri ->
                        viewModel.removeAttachment(uri)
                    }
                )
            }
        }
    }
}

@Composable
fun RemoveNoteButton() {
    val viewModel = hiltViewModel<NoteViewModel>()
    val state = viewModel.state.collectAsState().value
    val note = state.note
    IconButton(
        onClick = {
            viewModel.removeNote(note)
        }
    ) {
        Icon(
            ImageVector.vectorResource(R.drawable.ic_trash),
            "remove",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun EditModeButton() {
    val viewModel = hiltViewModel<NoteViewModel>()
    val state = viewModel.state.collectAsState().value
    val note = state.note
    val isEdit = state.isEdit
    if (note.id == null) return
    IconButton(
        onClick = {
            viewModel.switchMode()
        }
    ) {
        Icon(
            if (isEdit) Icons.Default.Close else Icons.Default.Edit,
            "edit",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}
