package com.vanyscore.notes.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteSectionsScreen() {
    val sections = listOf(
        "Meeting Notes",
        "Project Ideas",
        "Shopping List",
        "Travel Plans",
        "Learning Goals",
        "Daily Journal",
        "Recipe Collection",
        "Book Summaries",
        "Workout Routine",
        "Creative Writing"
    )
    val scrollState = rememberScrollState()
    return Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Заметки")
            })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .scrollable(scrollState, orientation = Orientation.Vertical)
        ) {
            sections.map { section ->
                NoteSection(title = section) { }
            }
        }
    }
}