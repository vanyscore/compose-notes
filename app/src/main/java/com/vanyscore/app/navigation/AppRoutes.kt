package com.vanyscore.app.navigation

object AppRoutes {
    const val MAIN = "main"
    const val TASKS = "tasks"
    const val NOTE = "note?id={${AppRouteArgs.NOTE_ID}}"
    const val NOTES = "notes"
    const val SETTINGS = "settings"
}

object AppRouteArgs {
    const val NOTE_ID = "noteId"
}

object AppRoutSchemes {
    fun note(id: Int? = null): String {
        var path = AppRoutes.NOTE
        if (id != null) {
            path = path.replace("{${AppRouteArgs.NOTE_ID}}", id.toString())
        }
        return path
    }
}