package com.vanyscore.notes.viewmodel

import android.net.Uri
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanyscore.notes.data.INoteRepo
import com.vanyscore.notes.domain.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

data class NoteState(
    val note: Note,
    val canClose: Boolean = false,
    val isEdit: Boolean = true,
)

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repo: INoteRepo
) : ViewModel() {

    private var noteId: Int? = null
    private var sectionId: Int? = null

    private val _state = MutableStateFlow(NoteState(
        note = Note()
    ))
    val state = _state.asStateFlow()

    private fun refresh() {
        val noteId = this.noteId ?: return
        viewModelScope.launch {
            val note = repo.getNote(noteId)
            if (note != null) {
                _state.update {
                    it.copy(
                        note = note,
                        isEdit = false
                    )
                }
            }
        }
    }


    fun attachArgs(noteId: Int?, sectionId: Int?) {
        this.noteId = noteId
        this.sectionId = sectionId

        if (sectionId != null) {
            _state.update {
                it.copy(
                    note = _state.value.note.copy(
                        sectionId = sectionId
                    )
                )
            }
        }

        refresh()
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

    fun attachAttachment(uri: Uri, note: Note) {
        viewModelScope.launch {
            val updatedNote = repo.attachImage(note, uri)
            if (updatedNote != null) {
                updateNote(updatedNote)
            }
        }
    }

    fun removeAttachment(uri: Uri) {
        viewModelScope.launch {
            val state = _state.value
            val note = state.note
            val images = note.images
            val updatedImages = images.toMutableList().apply {
                removeIf {
                    it.uri == uri
                }
            }
            _state.update {
                state.copy(
                    note = note.copy(
                        images = updatedImages
                    )
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

    fun removeNote(note: Note) {
        if (note.id != null) {
            viewModelScope.launch {
                note.images.forEach {
                    withContext(Dispatchers.IO) {
                        it.uri.toFile().delete()
                    }
                }
                repo.deleteNote(note)
                _state.update {
                    it.copy(
                        canClose = true
                    )
                }
            }
        }
    }

    fun switchMode() {
        val isEdit = !_state.value.isEdit
        _state.update {
            it.copy(
                isEdit = isEdit
            )
        }
    }
}