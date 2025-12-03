package com.vanyscore.notes.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanyscore.app.ui.SortState
import com.vanyscore.app.ui.SortType
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
    val sections: List<NoteSection>,
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
    private var sortState: SortState? = null

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            var sections = repo.getNoteSections()

            val sortState = sortState
            if (sortState != null) {
                val type = sortState.sortType
                val isAscending = sortState.isAscending
                if (type == SortType.ALPHABET) {
                    sections = if (isAscending) {
                        sections.sortedBy { it.name }
                    } else {
                        sections.sortedByDescending { it.name }
                    }
                } else if (type == SortType.DATE) {
                    sections = if (isAscending) {
                        sections.sortedBy { it.updatedDate }
                    } else {
                        sections.sortedByDescending { it.updatedDate }
                    }
                }
            }

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

    fun updateSortState(newState: SortState) {
        this.sortState = newState
        refresh()
    }
}