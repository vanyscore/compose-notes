package com.vanyscore.notes.data

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import com.vanyscore.app.domain.EventBus
import com.vanyscore.app.utils.DateUtils
import com.vanyscore.notes.domain.Note
import com.vanyscore.notes.domain.NoteSection
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

    private val _noteSections = mutableStateListOf(
        "Meeting Notes",
        "Project Ideas",
        "Shopping List",
        "Travel Plans",
        "Learning Goals",
        "Daily Journal",
        "Recipe Collection",
        "Book Summaries",
        "Workout Routine",
        "Creative Writing"
    ).mapIndexed { index, title ->
        NoteSection(index, title)
    }.toMutableList()

    override suspend fun getNoteSections(): List<NoteSection> {
        return mutableListOf<NoteSection>().apply {
            addAll(_noteSections)
        }
    }

    override suspend fun createNoteSection(name: String): NoteSection {
        val newSection = NoteSection(_noteSections.size + 1, name)
        _noteSections.add(newSection)
        return newSection
    }

    override suspend fun editNoteSection(noteSection: NoteSection): NoteSection {
        val foundSection = _noteSections.firstOrNull {
            it.id == noteSection.id
        }
        val index = _noteSections.indexOf(foundSection)
        _noteSections.removeAt(index)
        _noteSections.add(index, noteSection)
        return noteSection
    }

    override suspend fun deleteNoteSection(id: Int) {
        _noteSections.removeIf {
            it.id == id
        }
    }

    override suspend fun createNote(note: Note) {
        _notes.add(note.copy(
            id = ++_id
        ))
        EventBus.triggerNotesUpdated()
    }

    override suspend fun attachImage(note: Note, uri: Uri): Note? {
        return null
    }

    override suspend fun getNotes(fromDate: Date, toDate: Date): List<Note> {
        // TODO: Доработать (from-to: Date).
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
            DateUtils.isDateEqualsByDay(it.created, fromDate)
        }
    }

    override suspend fun getNotes(date: Date): List<Note> {
        return emptyList()
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
        EventBus.triggerNotesUpdated()
        return true
    }
}