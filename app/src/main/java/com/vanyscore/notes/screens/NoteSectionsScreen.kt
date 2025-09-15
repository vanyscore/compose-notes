package com.vanyscore.notes.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vanyscore.app.LocalInnerNavController
import com.vanyscore.app.navigation.AppRoutes
import com.vanyscore.notes.domain.NoteSection
import com.vanyscore.notes.ui.NoteSection
import com.vanyscore.notes.viewmodel.NoteSectionsViewModel
import com.vanyscore.tasks.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteSectionsScreen(
    viewModel: NoteSectionsViewModel = hiltViewModel(),
    navController: NavController = LocalInnerNavController.current
) {
    val state = viewModel.state.collectAsState().value
    val sections = state.sections
    val sectionDialogState = remember {
        mutableStateOf(NoteSectionDialogState(isVisible = false, type = NoteSectionDialogType.ADD))
    }
    return Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Заметки", style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp
                ))
            }, colors = TopAppBarDefaults .topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ), actions = {
                IconButton(onClick = {
                    sectionDialogState.value = sectionDialogState.value.copy(
                        section = null,
                        isVisible = true,
                        type = NoteSectionDialogType.ADD,
                    )
                }) {
                    Icon(Icons.Default.Add,
                        contentDescription = "add_note_section",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            })
        }
    ) { padding ->
        if (sections.isEmpty()) {
            SectionsListEmpty(padding)
        } else {
            SectionsList(padding, dialogState = sectionDialogState)
        }
        if (sectionDialogState.value.isVisible) {
            NoteSectionDialog(
                section = sectionDialogState.value.section,
                type = sectionDialogState.value.type,
                onDismiss = {
                    sectionDialogState.value = sectionDialogState.value.copy(
                        isVisible = false,
                    )
                }
            ) { name ->
                if (sectionDialogState.value.type == NoteSectionDialogType.ADD) {
                    viewModel.attachNoteSection(name)
                } else {
                    val originSection = sectionDialogState.value.section
                    if (originSection != null) {
                        viewModel.editNoteSection(
                            originSection.copy(
                                name = name,
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionsListEmpty(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .padding(0.dp, padding.calculateTopPadding(), 0.dp, 0.dp)
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Menu,
                modifier = Modifier.size(48.dp),
                contentDescription = "note_sections_empty"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Список разделов пуст", style = TextStyle(
                fontSize = 16.sp
            ))
        }
    }
}

@Composable
fun SectionsList(
    padding: PaddingValues,
    viewModel: NoteSectionsViewModel = hiltViewModel(),
    dialogState: MutableState<NoteSectionDialogState>,
) {
    val scrollState = rememberScrollState()
    val state = viewModel.state.collectAsState().value
    val sections = state.sections
    val navController = LocalInnerNavController.current
    Column(
        modifier = Modifier
            .padding(0.dp, padding.calculateTopPadding(), 0.dp, 0.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        sections.map { section ->
            key(section.name) {
                NoteSection(section = section, onLongClick = {
                    dialogState.value = dialogState.value.copy(
                        section = section,
                        isVisible = true,
                        type = NoteSectionDialogType.EDIT
                    )
                }, onClick = {
                    navController.navigate(AppRoutes.notes(section.id))
                }, onRemove = {
                    viewModel.deleteNoteSection(section)
                })
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.surface)
                )
            }
        }
    }
}

enum class NoteSectionDialogType {
    ADD, EDIT
}

data class NoteSectionDialogState(
    val section: NoteSection? = null,
    val isVisible: Boolean,
    val type: NoteSectionDialogType
)

@Composable
fun NoteSectionDialog(
    section: NoteSection?,
    type: NoteSectionDialogType,
    onDismiss: () -> Unit,
    onResult: (String) -> Unit
) {
    val sectionName = section?.name ?: ""
    val oldTitle = remember {
        "" + sectionName
    }
    return Dialog(
        onDismissRequest = onDismiss,
    ) {
        val text = remember { mutableStateOf(oldTitle) }
        val title = if (type == NoteSectionDialogType.ADD) stringResource(R.string.new_note_section) else stringResource(R.string.edit_note_section)
        val subtitle = if (type == NoteSectionDialogType.ADD) stringResource(R.string.add) else stringResource(R.string.apply)
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))
                .padding(18.dp)
        ) {
            Text(title,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                text.value,
                modifier = Modifier.height(56.dp),
                onValueChange = { newText ->
                    text.value = newText
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                onClick = {
                    onResult.invoke(text.value)
                    onDismiss()
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
                ),
                enabled = text.value.isNotEmpty()) {
                    Text(subtitle, style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp
                    ))
            }
        }
    }
}