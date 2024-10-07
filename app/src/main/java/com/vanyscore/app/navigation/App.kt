package com.vanyscore.app.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vanyscore.app.MainScreen
import com.vanyscore.notes.NoteScreen
import com.vanyscore.notes.domain.Note
import java.net.URI

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
                    navController.navigate(AppRouteSchemes.note(note?.id))
                }
            )
        }
        composable(AppRoutes.NOTE) {
            NoteScreen(null)
        }
    }
}