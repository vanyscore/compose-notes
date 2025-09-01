package com.vanyscore.notes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
                Text("Заметки", style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp
                ))
            }, colors = TopAppBarDefaults .topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ))
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(0.dp, padding.calculateTopPadding(), 0.dp, 0.dp)
                .fillMaxSize()
                .scrollable(scrollState, orientation = Orientation.Vertical)
        ) {
            sections.map { section ->
                NoteSection(title = section) { }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.surface))
            }
        }
    }
}