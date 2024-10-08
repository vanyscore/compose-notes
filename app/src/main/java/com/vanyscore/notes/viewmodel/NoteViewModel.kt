package com.vanyscore.notes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanyscore.app.Services
import com.vanyscore.notes.data.INoteRepo
import com.vanyscore.notes.domain.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteViewModel(
    private val repo: INoteRepo = Services.notesRepo
) : ViewModel() {

    private var noteId: Int? = null

    private val _state = MutableStateFlow(Note())
    val state = _state.asStateFlow()


    fun applyNoteId(noteId: Int) {
        this.noteId = noteId

        viewModelScope.launch {
            val note = repo.getNote(noteId) ?: return@launch
            _state.update {
                note
            }
        }
    }

    fun updateNote(copy: Note) {
        viewModelScope.launch {
            repo.updateNote(copy)
            _state.update {
                copy
            }
        }
    }
}