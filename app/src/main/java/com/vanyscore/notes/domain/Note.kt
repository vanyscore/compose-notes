package com.vanyscore.notes.domain

import android.net.Uri
import com.vanyscore.app.utils.FileUtil
import com.vanyscore.app.utils.Logger
import java.io.File
import java.util.Calendar
import java.util.Date

data class Note(
    val id: Int? = null,
    val sectionId: Int? = null,
    val title: String = "",
    val description: String = "",
    val created: Date = Calendar.getInstance().time,
    val edited: Date = Calendar.getInstance().time,
    val images: List<NoteImage> = listOf(),
)

data class NoteImage(
    val uri: Uri,
    val isTemporary: Boolean
)

suspend fun List<NoteImage>.moveIfItTemporary(outputDir: File): List<NoteImage> {
        return map {
        if (it.isTemporary) {
            val movedFileUri = FileUtil.copyFileByUri(it.uri, outputDir) ?: return@map null
            Logger.log("Move from ${it.uri} to $movedFileUri")
            it.copy(
                uri = movedFileUri,
                isTemporary = false,
            )
        } else {
            it
        }
    }.filterNotNull()
}