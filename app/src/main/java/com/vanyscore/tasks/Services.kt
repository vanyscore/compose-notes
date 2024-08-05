package com.vanyscore.tasks

import com.vanyscore.tasks.data.ITaskRepo
import com.vanyscore.tasks.data.TaskRepoInMemory

object Services {
    val tasksRepo: ITaskRepo = TaskRepoInMemory()
}