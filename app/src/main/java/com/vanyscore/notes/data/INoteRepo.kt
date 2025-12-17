package com.vanyscore.notes.data

import android.net.Uri
import com.vanyscore.notes.domain.Note
import com.vanyscore.notes.domain.NoteSection
import java.util.Date

interface INoteRepo {
    suspend fun createNote(note: Note)
    suspend fun attachImage(note: Note, uri: Uri): Note?
    suspend fun getNotes(fromDate: Date, toDate: Date): List<Note>
    suspend fun getNotes(sectionId: Int): List<Note>
    suspend fun getNotes(date: Date): List<Note>
    suspend fun getNote(id: Int): Note?
    suspend fun updateNote(note: Note): Boolean
    suspend fun deleteNote(note: Note): Boolean
}