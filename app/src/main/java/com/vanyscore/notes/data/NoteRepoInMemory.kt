package com.vanyscore.notes.data

import android.content.ContentResolver
import android.net.Uri
import com.vanyscore.app.domain.EventBus
import com.vanyscore.app.utils.DateUtils
import com.vanyscore.app.utils.FileUtil
import com.vanyscore.app.utils.Logger
import com.vanyscore.notes.domain.Note
import com.vanyscore.notes.domain.NoteImage
import java.io.File
import java.util.Date
import java.util.UUID

class NoteRepoInMemory(
    private val contentResolver: ContentResolver,
    private val cacheDir: File,
) : INoteRepo {

    private var _id = 0
    val notes = mutableListOf<Note>()

    override suspend fun createNote(note: Note) {
        Logger.log("original notes: $notes")
        notes.add(note.copy(
            id = ++_id
        ))
        Logger.log("updated notes: $notes")
        EventBus.triggerNotesUpdated()
    }

    override suspend fun attachImage(note: Note, uri: Uri): Note? {
        val attachmentUUID = UUID.randomUUID()
        val attachmentFileExtension = FileUtil.getFileExtensionFromUri(contentResolver, uri)
        val fileName = "${attachmentUUID}.$attachmentFileExtension"
        // Save image in cache dir.
        val savedFileUri = FileUtil
            .saveFileToInternalStorage(contentResolver, uri, fileName, dir = cacheDir) ?: return null
        return note.copy(
            images = note.images.toMutableList().apply {
                add(NoteImage(savedFileUri, isTemporary = true))
            }
        )
    }

    override suspend fun getNotes(fromDate: Date, toDate: Date): List<Note> {
        // TODO: Доработать (from-to: Date).
        return notes.filter {
            DateUtils.isDateEqualsByDay(it.created, fromDate)
        }
    }

    override suspend fun getNotes(sectionId: Int): List<Note> {
        Logger.log(this.notes.toString())
        return notes.filter { it.sectionId == sectionId }
    }

    override suspend fun getNotes(date: Date): List<Note> {
        return notes.filter { DateUtils.isDateEqualsByDay(it.created, date) }
    }

    override suspend fun getNote(id: Int): Note? {
        return notes.firstOrNull {
            it.id == id
        }
    }

    override suspend fun updateNote(note: Note): Boolean {
        val found = notes.firstOrNull {
            it.id == note.id
        }
        val index = notes.indexOf(found)
        if (index == -1) return false
        notes.removeAt(index)
        notes.add(index, note)
        EventBus.triggerNotesUpdated()
        return true
    }

    override suspend fun deleteNote(note: Note): Boolean {
        val found = notes.firstOrNull { it.id == note.id }
        if (found == null) return false
        val index = notes.indexOf(found)
        notes.removeAt(index)
        EventBus.triggerNotesUpdated()
        return true
    }
}