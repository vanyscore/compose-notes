package com.vanyscore.notes.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanyscore.app.Services
import com.vanyscore.notes.data.INoteRepo
import com.vanyscore.notes.domain.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

data class NoteState(
    val note: Note,
    val canClose: Boolean = false,
)

class NoteViewModel(
    private val repo: INoteRepo = Services.notesRepo
) : ViewModel() {

    private var noteId: Int? = null

    private val _state = MutableStateFlow(NoteState(
        note = Note()
    ))
    val state = _state.asStateFlow()


    fun applyNoteId(noteId: Int) {
        this.noteId = noteId

        viewModelScope.launch {
            val note = repo.getNote(noteId) ?: return@launch
            _state.update {
                it.copy(
                    note = note
                )
            }
        }
    }

    fun updateNote(copy: Note) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    note = copy
                )
            }
        }
    }

    fun saveNote(forDate: Date) {
        viewModelScope.launch {
            val note = _state.value.note
            val currentDate = Calendar.getInstance().time
            if (note.id == null) {
                repo.createNote(note.copy(
                    created = forDate,
                    edited = forDate
                ))
            } else {
                repo.updateNote(note.copy(
                    edited = currentDate
                ))
            }
            _state.update {
                it.copy(
                    canClose = true
                )
            }
        }
    }
}