package com.vanyscore.app.navigation
object AppRoutes {
    const val MAIN = "main"
    const val TASKS = "tasks"
    const val NOTE = "note?id={${AppRouteArgs.NOTE_ID}}&sectionId={${AppRouteArgs.SECTION_ID}}"
    const val NOTES = "notes?sectionId={${AppRouteArgs.SECTION_ID}}"
    const val NOTES_SECTIONS = "notes_sections"
    const val SETTINGS = "settings"

    fun note(id: Int? = null, sectionId: Int? = null): String {
        var path = NOTE
        path = path.replace("{${AppRouteArgs.NOTE_ID}}", id.toString())
        path = path.replace("{${AppRouteArgs.SECTION_ID}}", sectionId.toString())
        return path
    }

    fun notes(sectionId: Int?): String {
        return NOTES.replace("{${AppRouteArgs.SECTION_ID}}", sectionId.toString())
    }
}

object AppRouteArgs {
    const val NOTE_ID = "noteId"
    const val SECTION_ID = "sectionId"
}

