package com.vanyscore.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vanyscore.app.navigation.AppRouteArgs
import com.vanyscore.app.navigation.AppRoutes
import com.vanyscore.app.navigation.LocalRootNavController
import com.vanyscore.app.navigation.openNote
import com.vanyscore.app.ui.AppBottomBar
import com.vanyscore.notes.screens.NoteSectionsScreen
import com.vanyscore.notes.screens.NotesScreen
import com.vanyscore.tasks.ui.TasksPage

var LocalInnerNavController = staticCompositionLocalOf<NavHostController> {
    error("No navController provided")
}

@Composable
fun MainScreenWithBottomBar() {
    val rootNavController = LocalRootNavController.current
    val navController = rememberNavController()
    return Scaffold(
        modifier = Modifier.padding(0.dp),
        bottomBar = { AppBottomBar(navController) }
    ) { padding ->
        CompositionLocalProvider(
            LocalInnerNavController provides navController
        ) {
            NavHost(
                // TODO(vanyscore): Check it.
                modifier = Modifier.padding(top = 0.dp, start = padding.calculateLeftPadding(
                    LocalLayoutDirection.current), end = padding.calculateRightPadding(
                    LocalLayoutDirection.current), bottom = padding.calculateBottomPadding()),
                navController = navController,
                startDestination = AppRoutes.NOTES_SECTIONS
            ) {
                composable(AppRoutes.NOTES, arguments = listOf(
                    navArgument(AppRouteArgs.SECTION_ID) { type = NavType.IntType; }
                )) { entry ->
                    val sectionId = entry.arguments?.getInt(AppRouteArgs.SECTION_ID)
                    NotesScreen(
                        sectionId =  sectionId
                    ) { note ->
                        rootNavController.openNote(note)
                    }
                }
                composable(AppRoutes.NOTES_SECTIONS) {
                    NoteSectionsScreen()
                }
                composable(AppRoutes.TASKS) {
                    TasksPage()
                }
            }
        }
    }
}