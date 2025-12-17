package com.vanyscore.notes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanyscore.app.domain.Event
import com.vanyscore.app.domain.EventBus
import com.vanyscore.app.ui.DatesSelectedChecker
import com.vanyscore.app.utils.Logger
import com.vanyscore.app.viewmodel.AppViewModel
import com.vanyscore.notes.data.INoteRepo
import com.vanyscore.notes.domain.Note
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

data class NotesState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel(assistedFactory = NotesViewModel.Factory::class)
class NotesViewModel @AssistedInject constructor(
    private val repo: INoteRepo,
    private val appViewModel: AppViewModel,
    @Assisted
    private val sectionId: Int? = null,
) : ViewModel(), DatesSelectedChecker {

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
            appViewModel.state.collect { appState ->
                _currentDate = appState.date
                refresh()
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            val state = _state.value
            _state.update {
                state.copy(
                    isLoading = true
                )
            }
            val currentDate = _currentDate ?: Calendar.getInstance().time
            val sectionId = sectionId
            val notes = if (sectionId == null) {
                repo.getNotes(currentDate)
            } else {
                Logger.log("Get notes by sectionId: $sectionId")
                repo.getNotes(sectionId)
            }
            _state.update {
                state.copy(
                    notes = notes,
                    isLoading = false
                )
            }
        }
    }

    override suspend fun getSelectedDatesAsMillis(startDate: Date, endDate: Date): List<Long> {
        val notes = repo.getNotes(fromDate = startDate, toDate = endDate)
        return notes.map { dt -> dt.created.time }
    }

    @AssistedFactory
    interface Factory {
        fun create(sectionId: Int?): NotesViewModel
    }
}