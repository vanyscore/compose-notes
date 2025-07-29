package com.vanyscore.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vanyscore.app.navigation.AppRoutes
import com.vanyscore.app.navigation.LocalMainNavController
import com.vanyscore.app.navigation.openNote
import com.vanyscore.app.ui.AppBottomBar
import com.vanyscore.app.ui.DatePickerBar
import com.vanyscore.notes.NotesPage
import com.vanyscore.tasks.ui.TasksPage

@Composable
fun MainScreenWithBottomBar() {
    val mainNavController = LocalMainNavController.current
    val navController = rememberNavController()
    return Scaffold(
        topBar = {
            DatePickerBar()
        },
        bottomBar = { AppBottomBar(navController) }
    ) { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            startDestination = AppRoutes.NOTES
        ) {
            composable(AppRoutes.NOTES) {
                NotesPage(openNote = { note ->
                    mainNavController.openNote(note)
                })
            }
            composable(AppRoutes.TASKS) {
                TasksPage()
            }
        }
    }
}