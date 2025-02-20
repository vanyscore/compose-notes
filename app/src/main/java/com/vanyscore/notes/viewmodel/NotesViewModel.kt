package com.vanyscore.notes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanyscore.app.AppState
import com.vanyscore.app.domain.Event
import com.vanyscore.app.domain.EventBus
import com.vanyscore.notes.data.INoteRepo
import com.vanyscore.notes.domain.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

data class NotesState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
)

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repo: INoteRepo
) : ViewModel() {

    private val _state = MutableStateFlow(NotesState())
    val state = _state.asStateFlow()

    private var _currentDate: Date? = null

    init {
        viewModelScope.launch {
            EventBus.eventsSource.collect {
                if (it == Event.NOTES_UPDATED) {
                    refresh()
                }
            }
        }
        viewModelScope.launch {
            AppState.source.collect { appState ->
                _currentDate = appState.date
                refresh()
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            val state = _state.value
            _state.update {
                state.copy(
                    isLoading = true
                )
            }
            val currentDate = this@NotesViewModel._currentDate ?: Calendar.getInstance().time
            val calendar = Calendar.getInstance()
            val startDate = calendar.apply {
                time = currentDate
                set(Calendar.HOUR, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }.time
            val endDate = calendar.apply {
                time = currentDate
                set(Calendar.HOUR, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
            }.time
            val notes = repo.getNotes(startDate, endDate)
            _state.update {
                state.copy(
                    notes = notes,
                    isLoading = false
                )
            }
        }
    }
}