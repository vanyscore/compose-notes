package com.vanyscore.tasks.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanyscore.tasks.Services
import com.vanyscore.tasks.data.ITaskRepo
import com.vanyscore.tasks.data.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class MainViewModel(
    private val repository: ITaskRepo = Services.tasksRepo
) : ViewModel() {

    private val _state = MutableStateFlow(MainViewState())
    val state: StateFlow<MainViewState>
        get() = _state.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            val tasks = repository.getTasks(Calendar.getInstance().time)
            _state.value = MainViewState(tasks = tasks)
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

}

data class MainViewState(
    val date: Date = Calendar.getInstance().time,
    val tasks: List<Task> = listOf()
)