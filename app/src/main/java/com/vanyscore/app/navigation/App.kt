package com.vanyscore.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vanyscore.app.AppState
import com.vanyscore.app.MainScreen
import com.vanyscore.notes.NoteScreen
import com.vanyscore.notes.domain.Note

@Composable
fun App() {
    val navController = rememberNavController()
    AppState.bindNavController(navController)
    return NavHost(
        navController = navController,
        startDestination = AppRoutes.MAIN
    ) {
        composable(AppRoutes.MAIN) {
            MainScreen(
                openNote = { note: Note? ->
                    navController.openNote(note)
                }
            )
        }
        composable(AppRoutes.NOTE, listOf(
            navArgument(AppRouteArgs.NOTE_ID) {
                type = NavType.IntType
                defaultValue = -1
            }
        )) {
            val noteId = it.arguments?.getInt(AppRouteArgs.NOTE_ID)
            NoteScreen(if (noteId == -1) null else noteId)
        }
    }
}

fun NavController.openNote(note: Note?) {
    navigate(AppRoutSchemes.note(note?.id))
}