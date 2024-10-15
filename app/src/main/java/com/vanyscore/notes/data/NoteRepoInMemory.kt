package com.vanyscore.notes.data

import com.vanyscore.app.domain.EventBus
import com.vanyscore.app.utils.DateUtils
import com.vanyscore.notes.domain.Note
import java.util.Calendar
import java.util.Date

class NoteRepoInMemory : INoteRepo {

    private var _id = 0
    private val _notes = mutableListOf(
        Note(
            ++_id,
            "Заметка",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.",
            created = Calendar.getInstance().time,
            edited = Calendar.getInstance().time
        ),
        Note(
            ++_id,
            "Заметка (доп)",
            "'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).",
            created = Calendar.getInstance().time,
            edited = Calendar.getInstance().time
        )
    )

    override suspend fun createNote(note: Note) {
        _notes.add(note.copy(
            id = ++_id
        ))
        EventBus.triggerNotesUpdated()
    }

    override suspend fun getNotes(date: Date): List<Note> {
        if (_notes.isEmpty()) {
            repeat(10) { index ->
                _notes.add(
                    Note(
                    id = ++_id,
                    title = "Note $index",
                    description = "Som note",
                    created = Calendar.getInstance().time,
                    edited = Calendar.getInstance().time,
                )
                )
            }
        }
        return _notes.filter {
            DateUtils.isDateEqualsByDay(it.created, date)
        }
    }

    override suspend fun getNote(id: Int): Note? {
        return _notes.firstOrNull {
            it.id == id
        }
    }

    override suspend fun updateNote(note: Note): Boolean {
        val found = _notes.firstOrNull {
            it.id == note.id
        }
        val index = _notes.indexOf(found)
        if (index == -1) return false
        _notes.removeAt(index)
        _notes.add(index, note)
        EventBus.triggerNotesUpdated()
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