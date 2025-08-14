package com.vanyscore.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vanyscore.app.navigation.AppRoutes
import com.vanyscore.app.navigation.LocalMainNavController
import com.vanyscore.app.navigation.openNote
import com.vanyscore.app.ui.AppBottomBar
import com.vanyscore.notes.NotesPage
import com.vanyscore.tasks.ui.TasksPage

@Composable
fun MainScreenWithBottomBar() {
    val mainNavController = LocalMainNavController.current
    val navController = rememberNavController()
    return Scaffold(
        modifier = Modifier.padding(0.dp),
        bottomBar = { AppBottomBar(navController) }
    ) { padding ->
        NavHost(
            // TODO(vanyscore): Check it.
            modifier = Modifier.padding(top = 0.dp, start = padding.calculateLeftPadding(
                LocalLayoutDirection.current), end = padding.calculateRightPadding(
                LocalLayoutDirection.current), bottom = padding.calculateBottomPadding()),
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