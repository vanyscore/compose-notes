package com.vanyscore.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vanyscore.app.MainScreenWithBottomBar
import com.vanyscore.notes.NoteScreen
import com.vanyscore.notes.domain.Note
import com.vanyscore.notes.ui.NoteSectionsScreen
import com.vanyscore.settings.SettingsScreen

var LocalMainNavController = staticCompositionLocalOf<NavHostController> {
    error("No navController provided")
}

@Composable
fun App() {
    val navController = rememberNavController()

    CompositionLocalProvider(
        LocalMainNavController provides navController
    ) {
        NavHost(
            navController = LocalMainNavController.current,
            startDestination = AppRoutes.NOTES_SECTIONS
        ) {
            composable(AppRoutes.MAIN) {
                MainScreenWithBottomBar()
            }
            composable(AppRoutes.NOTES_SECTIONS) {
                NoteSectionsScreen()
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
            composable(AppRoutes.SETTINGS) {
                SettingsScreen()
            }
        }
    }
}

fun NavController.openSettings() {
    navigate(AppRoutes.SETTINGS)
}

fun NavController.openNote(note: Note?) {
    navigate(AppRoutSchemes.note(note?.id))
}