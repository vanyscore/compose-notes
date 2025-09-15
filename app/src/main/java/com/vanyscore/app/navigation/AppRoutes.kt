package com.vanyscore.app.navigation

object AppRoutes {
    const val MAIN = "main"
    const val TASKS = "tasks"
    const val NOTE = "note?id={${AppRouteArgs.NOTE_ID}}"
    const val NOTES = "notes?sectionId={${AppRouteArgs.SECTION_ID}}"
    const val NOTES_SECTIONS = "notes_sections"
    const val SETTINGS = "settings"

    fun notes(sectionId: Int?): String {
        return NOTES.replace("{${AppRouteArgs.SECTION_ID}}", sectionId.toString())
    }
}

object AppRouteArgs {
    const val NOTE_ID = "noteId"
    const val SECTION_ID = "sectionId"
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