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
        Log.d("note", "applyNoteId")

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

    fun saveNote() {
        viewModelScope.launch {
            val note = _state.value.note
            if (note.id == null) {
                repo.createNote(note)
            } else {
                repo.updateNote(note)
            }
            _state.update {
                it.copy(
                    canClose = true
                )
            }
        }
    }
}