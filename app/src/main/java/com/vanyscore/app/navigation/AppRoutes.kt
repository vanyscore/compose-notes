package com.vanyscore.app.navigation

object AppRoutes {
    const val MAIN = "main"
    const val NOTE = "note"
}

object AppRouteSchemes {
    fun note(noteId: Int? = null): String {
        return if (noteId == null) {
            AppRoutes.NOTE
        } else {
            AppRoutes.NOTE + "?noteId=$noteId"
        }
    }
}