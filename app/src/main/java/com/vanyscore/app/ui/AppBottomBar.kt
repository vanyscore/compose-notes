package com.vanyscore.app.ui

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vanyscore.app.navigation.AppRoutes

private class AppRoute(
    val title: String,
    val icon: ImageVector,
    val routePath: String,
)

@Composable
fun AppBottomBar(
    navController: NavController
) {
    val routes = listOf(
        AppRoute(title = "Заметки", icon = Icons.Default.Edit, routePath = AppRoutes.NOTES_SECTIONS),
        AppRoute(title = "Задачи", icon = Icons.Default.Build, routePath = AppRoutes.TASKS),
    )
    val routeState = remember { mutableStateOf(AppRoutes.NOTES_SECTIONS) }
    val currentRoute = routeState.value
    Log.d("nav", "curr route: $currentRoute, ${routes.map { it.routePath }}")
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        tonalElevation = 2.dp,
        modifier = Modifier.height(116.dp)
    ) {
        routes.forEach { route ->
            NavigationBarItem(currentRoute == route.routePath, onClick = {
                navController.navigate(route.routePath)
                routeState.value = route.routePath
            }, icon = {
                Icon(
                    route.icon,
                    modifier = Modifier
                        .size(16.dp),
                    contentDescription = "notes",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }, label = {
                Text(route.title, style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary
                ))
            },)
        }
    }
}