package com.vanyscore.tasks.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanyscore.app.AppState
import com.vanyscore.app.Services
import com.vanyscore.app.utils.DateUtils
import com.vanyscore.tasks.data.ITaskRepo
import com.vanyscore.tasks.data.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class TaskViewModel(
    private val repository: ITaskRepo = Services.tasksRepo
) : ViewModel() {

    private val _state = MutableStateFlow(MainViewState())
    val state: StateFlow<MainViewState>
        get() = _state.asStateFlow()


    init {
        viewModelScope.launch {
            AppState.source.collect { appState ->
                _state.update {
                    _state.value.copy(
                        selectedDate = appState.date
                    )
                }
                refresh()
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            val state = _state.value
            val calendar = Calendar.getInstance().apply {
                time = state.selectedDate
            }
            val startDate = calendar.apply {
                set(Calendar.DAY_OF_MONTH, 1)
            }.time
            val endDate = calendar.apply {
                add(Calendar.MONTH, 1)
                set(Calendar.DAY_OF_MONTH, -1)
            }.time
            val monthDates = DateUtils.getAllDaysFromMonth(state.selectedDate)
            val monthTasks = repository.getTasks(startDate, endDate)
            _state.update {
                state.copy(
                    monthDates = monthDates,
                    monthTasks = monthTasks
                )
            }
        }
    }

    fun taskStatusUpdated(task: Task) {
        viewModelScope.launch {
            val isSuccess = repository.updateTask(task)
            if (isSuccess) {
                refresh()
            }
        }
    }

    fun createTask(task: Task) {
        viewModelScope.launch {
            repository.createTask(task)
            refresh()
        }
    }

    fun editTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
            refresh()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
            refresh()
        }
    }
}

data class MainViewState(
    val selectedDate: Date = Calendar.getInstance().time,
    val monthDates: List<Date> = emptyList(),
    val monthTasks: List<Task> = emptyList(),
)