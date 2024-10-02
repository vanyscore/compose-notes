package com.vanyscore.app

import com.vanyscore.tasks.data.ITaskRepo
import com.vanyscore.tasks.data.TaskRepoInMemory

object Services {
    val tasksRepo: ITaskRepo = TaskRepoInMemory()
}