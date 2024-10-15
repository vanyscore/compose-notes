package com.vanyscore.tasks.data

import com.vanyscore.app.utils.DateUtils
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

    private fun fillIfEmpty() {
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
    }

    override suspend fun getTasks(date: Date): List<Task> {
        fillIfEmpty()
        return _tasks.filter {
            DateUtils.isDateEqualsByDay(it.date, date)
        }
    }

    override suspend fun getTasks(startDate: Date, endDate: Date): List<Task> {
        fillIfEmpty()
        return _tasks.filter {
            it.date.after(startDate) && it.date.before(endDate)
        }
    }

    override suspend fun updateTask(task: Task): Boolean {
        val found = _tasks.firstOrNull {
            it.id == task.id
        }
        val index = _tasks.indexOf(found)
        if (index == -1) return false
        _tasks.removeAt(index)
        _tasks.add(index, task)
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