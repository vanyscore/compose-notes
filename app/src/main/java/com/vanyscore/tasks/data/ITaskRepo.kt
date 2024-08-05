package com.vanyscore.tasks.data

import java.util.Date

interface ITaskRepo {
    suspend fun createTask(task: Task)
    suspend fun getTasks(date: Date): List<Task>
    suspend fun updateTask(task: Task): Boolean
    suspend fun deleteTask(task: Task): Boolean
}