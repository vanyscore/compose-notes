package com.vanyscore.tasks.data

import com.vanyscore.tasks.utils.DateUtils
import java.util.Calendar
import java.util.Date

class NoteRepoInMemory : INoteRepo {

    private var _id = 0
    private val _notes = mutableListOf<Note>()

    override suspend fun createNote(note: Note) {
        _notes.add(note.copy(
            id = ++_id
        ))
    }

    override suspend fun getNotes(date: Date): List<Note> {
        if (_notes.isEmpty()) {
            repeat(10) { index ->
                _notes.add(Note(
                    id = ++_id,
                    title = "Note $index",
                    description = "Som note",
                    created = Calendar.getInstance().time,
                    edited = Calendar.getInstance().time,
                ))
            }
        }
        return _notes.filter {
            DateUtils.isDateEqualsByDay(it.edited, date)
        }
    }

    override suspend fun updateNote(note: Note): Boolean {
        val found = _notes.firstOrNull {
            it.id == note.id
        }
        val index = _notes.indexOf(found)
        val updated = found?.copy(
            title = note.title,
            description = note.description,
        )
        if (updated == null) return false
        _notes.removeAt(index)
        _notes.add(index, updated)
        return true
    }

    override suspend fun deleteNote(note: Note): Boolean {
        val found = _notes.firstOrNull { it.id == note.id }
        if (found == null) return false
        val index = _notes.indexOf(found)
        _notes.removeAt(index)
        return true
    }
}