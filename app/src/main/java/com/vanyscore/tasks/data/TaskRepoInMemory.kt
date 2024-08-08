package com.vanyscore.tasks.data

import android.util.Log
import com.vanyscore.tasks.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class TaskRepoInMemory : ITaskRepo {

    private var _id = 0
    private val _tasks = mutableListOf<Task>()

    override suspend fun createTask(task: Task) {
       _tasks.add(task.copy(
           id = ++_id
       ))
    }

    override suspend fun getTasks(date: Date): List<Task> {
        if (_tasks.isEmpty()) {
            repeat(10) { index ->
                _tasks.add(Task(
                    id = ++_id,
                    title = "Task $index",
                    isSuccess = false,
                    date = Calendar.getInstance().time,
                ))
            }
        }
        return _tasks.filter {
            DateUtils.compareByDay(it.date, date)
        }
    }

    override suspend fun updateTask(task: Task): Boolean {
        val found = _tasks.firstOrNull {
            it.id == task.id
        }
        val index = _tasks.indexOf(found)
        val updated = found?.copy(
            title = task.title,
            isSuccess = task.isSuccess,
            date = task.date,
        )
        if (updated == null) return false
        _tasks.removeAt(index)
        _tasks.add(index, updated)
        return true
    }

    override suspend fun deleteTask(task: Task): Boolean {
        val found = _tasks.firstOrNull { it.id == task.id }
        if (found == null) return false
        val index = _tasks.indexOf(found)
        _tasks.removeAt(index)
        return true
    }
}