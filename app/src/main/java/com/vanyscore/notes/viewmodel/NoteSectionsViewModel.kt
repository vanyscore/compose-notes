package com.vanyscore.notes.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanyscore.notes.data.INoteRepo
import com.vanyscore.notes.domain.NoteSection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NoteSectionsState(
    val isLoading: Boolean,
    val sections: List<NoteSection>
)

@HiltViewModel
class NoteSectionsViewModel @Inject constructor(
    private val repo: INoteRepo
): ViewModel() {
    private val _state = MutableStateFlow(NoteSectionsState(
        isLoading = true,
        sections = mutableListOf(),
    ))
    val state = _state.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            val sections = repo.getNoteSections()
            _state.update {
                it.copy(
                    isLoading = false,
                    sections = sections
                )
            }
        }
    }

    fun attachNoteSection(name: String) {
        viewModelScope.launch {
            repo.createNoteSection(name)
            refresh()
        }
    }

    fun editNoteSection(section: NoteSection) {
        viewModelScope.launch {
            repo.editNoteSection(section)
            refresh()
        }
    }

    fun deleteNoteSection(section: NoteSection) {
        viewModelScope.launch {
            repo.deleteNoteSection(section.id)
            refresh()
        }
    }
}