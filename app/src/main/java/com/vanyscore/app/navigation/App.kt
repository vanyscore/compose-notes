package com.vanyscore.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vanyscore.app.MainScreen
import com.vanyscore.notes.NoteScreen
import com.vanyscore.notes.domain.Note

@Composable
fun App() {
    val navController = rememberNavController()
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
            }
        )) {
            val noteId = it.arguments?.getInt(AppRouteArgs.NOTE_ID)
            NoteScreen(noteId)
        }
    }
}

fun NavController.openNote(note: Note?) {
    navigate(AppRoutSchemes.note(note?.id))
}